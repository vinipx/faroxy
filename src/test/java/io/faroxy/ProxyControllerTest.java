package io.faroxy;

import io.faroxy.controller.ProxyController;
import io.faroxy.service.FaroxyProxyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProxyController.class)
public class ProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FaroxyProxyService faroxyProxyService;

    @Test
    public void testProxyRequest() throws Exception {
        String responseBody = "{\"message\": \"Success\"}";
        when(faroxyProxyService.proxyRequest(any(), any(), any(), any()))
            .thenReturn(Mono.just(responseBody));

        mockMvc.perform(get("/proxy")
                .param("targetUrl", "https://api.example.com/test"))
                .andExpect(status().isOk());
    }
}
