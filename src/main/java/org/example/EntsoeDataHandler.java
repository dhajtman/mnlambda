package org.example;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.micronaut.context.ApplicationContext;
import io.micronaut.function.aws.MicronautRequestHandler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EntsoeDataHandler extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(EntsoeDataHandler.class);
    private final XmlMapper xmlMapper = new XmlMapper();

    private final S3Client s3Client;
    private final HttpClient httpClient;
    private final EntsoeConfig entsoeConfig;

    public EntsoeDataHandler() {
        this.s3Client = S3Client.create();
        this.httpClient = HttpClient.newHttpClient();
        this.entsoeConfig = getApplicationContext().getBean(EntsoeConfig.class);
    }

    public EntsoeDataHandler(S3Client s3Client, HttpClient httpClient, EntsoeConfig entsoeConfig) {
        this.s3Client = s3Client;
        this.httpClient = httpClient;
        this.entsoeConfig = entsoeConfig;
    }

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent input) {
        ApplicationContext context = getApplicationContext();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        LOG.info("Starting the Lambda function...");
        LOG.info("Event: " + input);
//        LOG.info("Environment Variables: " + System.getenv());
        LOG.info("System Properties: " + System.getProperties());

        try {
            String apiUrl = assemblyApiUrl();
            String sanitizedApiUrl = apiUrl.replaceAll("&?api_url_token=[^&]*", "");
            LOG.info("Sanitized API URL: " + sanitizedApiUrl);

            String responseData = fetchDataFromApi(apiUrl);
            LOG.info("Got response with size: " + responseData.length());

            List<String> processedData = processData(responseData, entsoeConfig.getTargetKey());
            LOG.info("Processed data: " + processedData);

            String csvData = String.join(",", processedData);

            String fileName = String.format("%s-%s.csv", entsoeConfig.getOutputPrefix(), Instant.now().toString());

            uploadToS3(entsoeConfig.getS3Bucket(), fileName, csvData);

            LOG.info("Data successfully uploaded to S3: " + fileName);

            response.setBody("Success");
            response.setStatusCode(200);

            return response;
        } catch (Exception e) {
            LOG.error("Error: " + e);
            throw new RuntimeException(e);
        }
    }

    private String assemblyApiUrl() {
        return entsoeConfig.getApiUrl()
                .replace("{document_type}", entsoeConfig.getDocumentType())
                .replace("{process_type}", entsoeConfig.getProcessType())
                .replace("{in_domain}", entsoeConfig.getInDomain())
                .replace("{period_start}", entsoeConfig.getPeriodStart())
                .replace("{period_end}", entsoeConfig.getPeriodEnd())
                .replace("{api_url_token}", entsoeConfig.getApiUrlToken());
    }

    private String fetchDataFromApi(String apiUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            LOG.error("Failed to fetch data from API: " + response.statusCode() + " " + response.body());
            throw new RuntimeException("Failed to fetch data from API: " + response.body());
        }
        return response.body();
    }

    public List<String> processData(String xmlData, String targetKey) throws Exception {
        Map<String, Object> root = xmlMapper.readValue(xmlData, Map.class);

        List<String> extractedValues = new ArrayList<>();
        extractValues(root, targetKey, extractedValues);
        return extractedValues;
    }

    private void extractValues(Object node, String targetKey, List<String> extractedValues) {
        if (node instanceof Map) {
            Map<String, Object> mapNode = (Map<String, Object>) node;
            for (Map.Entry<String, Object> entry : mapNode.entrySet()) {
                if (entry.getKey().equals(targetKey)) {
                    extractedValues.add(entry.getValue().toString());
                } else {
                    extractValues(entry.getValue(), targetKey, extractedValues);
                }
            }
        } else if (node instanceof List) {
            List<Object> listNode = (List<Object>) node;
            for (Object item : listNode) {
                extractValues(item, targetKey, extractedValues);
            }
        }
    }

    private void uploadToS3(String bucketName, String key, String data) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromString(data));
    }
}
