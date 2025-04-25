package org.example;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
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
    private static final S3Client S3_CLIENT = S3Client.builder().build();

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent input) {
        ApplicationContext context = getApplicationContext();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        LOG.info("Starting the Lambda function...");
        LOG.info("Event: " + input);
        LOG.info("Environment Variables: " + System.getenv());
        LOG.info("System Properties: " + System.getProperties());

        try {
            // Access environment variables
            String apiUrlTemplate = System.getenv().getOrDefault("API_URL",
                    "https://web-api.tp.entsoe.eu/api?documentType={document_type}&processType={process_type}&in_Domain={in_domain}&periodStart={period_start}&periodEnd={period_end}&securityToken={api_url_token}");
            String apiUrlToken = System.getenv().getOrDefault("API_URL_TOKEN", "xxxxxx");
            String documentType = System.getenv().getOrDefault("DOCUMENT_TYPE", "A75");
            String processType = System.getenv().getOrDefault("PROCESS_TYPE", "A16");
            String inDomain = System.getenv().getOrDefault("IN_DOMAIN", "10Y1001A1001A83F");
            String periodStart = System.getenv().getOrDefault("PERIOD_START", "202308152200");
            String periodEnd = System.getenv().getOrDefault("PERIOD_END", "202308162200");
            String targetKey = System.getenv().getOrDefault("TARGET_KEY", "quantity");

            String bucketName = System.getenv().getOrDefault("S3_BUCKET", "entsoe-data-buckets");
            String outputPrefix = System.getenv().getOrDefault("OUTPUT_PREFIX", "entsoe-data");

            String apiUrl = assembleApiUrl(apiUrlTemplate, documentType, processType, inDomain, periodStart, periodEnd, apiUrlToken);

            // Fetch data from the API
            String responseData = fetchDataFromApi(apiUrl);
            LOG.info("Got response: " + responseData);

            // Process the data dynamically
            List<String> processedData = processData(responseData, targetKey);
            LOG.info("Processed data: " + processedData);

            // Convert the data to CSV format
            String csvData = String.join(",", processedData);

            // Generate a unique file name
            String fileName = String.format("%s-%s.csv", outputPrefix, Instant.now().toString());

            // Upload the CSV to S3
            uploadToS3(bucketName, fileName, csvData);

            LOG.info("Data successfully uploaded to S3: " + fileName);

            response.setBody("Success");
            response.setStatusCode(200);

            return response;
        } catch (Exception e) {
            LOG.error("Error: " + e);
            throw new RuntimeException(e);
        }
    }

    private String assembleApiUrl(String apiUrl, String documentType, String processType, String inDomain, String periodStart, String periodEnd, String apiUrlToken) {
        return apiUrl.replace("{document_type}", documentType)
                .replace("{process_type}", processType)
                .replace("{in_domain}", inDomain)
                .replace("{period_start}", periodStart)
                .replace("{period_end}", periodEnd)
                .replace("{api_url_token}", apiUrlToken);
    }

    private String fetchDataFromApi(String apiUrl) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            LOG.error("Failed to fetch data from API: " + response.statusCode() + " " + response.body());
            throw new RuntimeException("Failed to fetch data from API: " + response.body());
        }
        return response.body();
    }

    public List<String> processData(String xmlData, String targetKey) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
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

        S3_CLIENT.putObject(putObjectRequest, RequestBody.fromString(data));
    }
}
