package org.example;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class EntsoeDataHandlerTest {

    @Mock
    private S3Client mockS3Client;

    @Mock
    private HttpClient mockHttpClient;

    @Inject
    private EntsoeConfig mockEntsoeConfig;

    private EntsoeDataHandler handler;

    @BeforeEach
    public void setupServer() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);

        when(mockS3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(null);

        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("<response>Mocked API Response</response>");

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);

        handler = new EntsoeDataHandler(mockS3Client, mockHttpClient, mockEntsoeConfig);
    }

    @AfterEach
    public void stopServer() {
        if (handler != null) {
            handler.getApplicationContext().close();
        }
    }

    @Test
    public void testHandler() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/");
        APIGatewayProxyResponseEvent response = handler.execute(request);
        assertEquals(200, response.getStatusCode().intValue());
        assertEquals("Success", response.getBody());
    }
}
