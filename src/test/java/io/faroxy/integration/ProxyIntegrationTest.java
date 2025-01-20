package io.faroxy.integration;

import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.repository.RequestLogRepository;
import io.faroxy.repository.ResponseLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProxyIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RequestLogRepository requestLogRepository;

    @Autowired
    private ResponseLogRepository responseLogRepository;

    private String getProxyUrl() {
        return "http://localhost:" + port + "/proxy";
    }

    @Test
    void testGetRequest() {
        // Given
        String targetUrl = "https://httpbin.org/get";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            getProxyUrl() + "?url=" + targetUrl,
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("Response body: " + response.getBody());
        assertThat(response.getBody()).contains("\"url\"");
        assertThat(response.getBody()).contains("/get");

        // Verify logs are stored
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            List<RequestLog> requests = requestLogRepository.findAll();
            List<ResponseLog> responses = responseLogRepository.findAll();
            
            assertThat(requests).isNotEmpty();
            assertThat(responses).isNotEmpty();
            
            RequestLog lastRequest = requests.get(requests.size() - 1);
            System.out.println("Request headers: " + lastRequest.getHeaders());
            System.out.println("Request body: " + lastRequest.getBody());
            
            assertThat(lastRequest.getUrl()).contains("/get");
            assertThat(lastRequest.getMethod()).isEqualTo("GET");
        });
    }

    @Test
    void testPostRequest() {
        // Given
        String targetUrl = "https://httpbin.org/post";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("testKey", "testValue");
        
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
        
        // When
        ResponseEntity<String> response = restTemplate.exchange(
            getProxyUrl() + "?url=" + targetUrl + "&method=POST",
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("Response body: " + response.getBody());
        assertThat(response.getBody()).contains("\"form\"").contains("\"testKey\": \"testValue\"");

        // Verify logs are stored
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            List<RequestLog> requests = requestLogRepository.findAll();
            List<ResponseLog> responses = responseLogRepository.findAll();
            
            assertThat(requests).isNotEmpty();
            assertThat(responses).isNotEmpty();
            
            RequestLog lastRequest = requests.get(requests.size() - 1);
            System.out.println("Request headers: " + lastRequest.getHeaders());
            System.out.println("Request body: " + lastRequest.getBody());
            
            assertThat(lastRequest.getUrl()).contains("/post");
            assertThat(lastRequest.getMethod()).isEqualTo("POST");
            assertThat(lastRequest.getBody()).contains("testKey=testValue");
            assertThat(lastRequest.getHeaders().toLowerCase())
                .contains("content-type=application/x-www-form-urlencoded");
        });
    }

    @Test
    void testCustomHeadersRequest() {
        // Given
        String targetUrl = "https://httpbin.org/headers";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Custom-Header", "test-value");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        // When
        ResponseEntity<String> response = restTemplate.exchange(
            getProxyUrl() + "?url=" + targetUrl,
            HttpMethod.GET,
            requestEntity,
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("Headers response body: " + response.getBody());
        assertThat(response.getBody()).contains("\"X-Custom-Header\"").contains("test-value");

        // Verify logs are stored
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            List<RequestLog> requests = requestLogRepository.findAll();
            assertThat(requests).isNotEmpty();
            
            RequestLog lastRequest = requests.get(requests.size() - 1);
            System.out.println("Headers request headers: " + lastRequest.getHeaders());
            
            assertThat(lastRequest.getHeaders().toLowerCase())
                .contains("x-custom-header=test-value");
        });
    }
}
