package io.faroxy.controller;

import io.faroxy.config.ProxyConfiguration;
import io.faroxy.service.FaroxyProxyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProxyController.class)
class ProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FaroxyProxyService proxyService;

    @MockBean
    private ProxyConfiguration proxyConfig;

    @Test
    void testProxyRequest() throws Exception {
        // Given
        String testUrl = "http://example.com";
        String expectedResponse = "Test Response";
        
        when(proxyService.proxyRequest(
                eq(testUrl), 
                eq(HttpMethod.GET), 
                eq(null), 
                any()))
            .thenReturn(Mono.just(expectedResponse));

        // When & Then
        mockMvc.perform(get("/proxy")
                .param("url", testUrl))
                .andExpect(status().isOk());
    }
}
