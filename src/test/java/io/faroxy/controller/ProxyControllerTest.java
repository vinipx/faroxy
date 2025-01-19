package io.faroxy.controller;

import io.faroxy.service.FaroxyProxyService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import jakarta.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProxyControllerTest {

    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    class IntegrationTests {
        @LocalServerPort
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @MockBean
        private FaroxyProxyService proxyService;

        @Test
        void testProxyRequest() {
            // Given
            String testUrl = "http://example.com";
            String expectedResponse = "Test Response";
            
            when(proxyService.proxyRequest(
                    eq(testUrl), 
                    eq(HttpMethod.GET), 
                    eq(null), 
                    any(HttpServletRequest.class)))
                .thenReturn(Mono.just(expectedResponse));

            // When
            ResponseEntity<String> response = restTemplate.getForEntity(
                    String.format("http://localhost:%d/proxy?url=%s", port, testUrl),
                    String.class);

            // Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo(expectedResponse);
        }
    }

    @Nested
    @WebMvcTest(ProxyController.class)
    class WebLayerTests {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private FaroxyProxyService proxyService;

        @Test
        void testProxyRequest() throws Exception {
            String responseBody = "{\"message\": \"Success\"}";
            when(proxyService.proxyRequest(any(), any(), any(), any()))
                .thenReturn(Mono.just(responseBody));

            mockMvc.perform(get("/proxy")
                    .param("url", "https://api.example.com/test"))
                    .andExpect(status().isOk());
        }
    }
}
