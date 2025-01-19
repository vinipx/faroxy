package io.faroxy.controller;

import io.faroxy.config.ProxyConfiguration;
import io.faroxy.service.FaroxyProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/proxy")
public class ProxyController {
    private final FaroxyProxyService proxyService;
    private final ProxyConfiguration proxyConfig;

    public ProxyController(FaroxyProxyService proxyService, ProxyConfiguration proxyConfig) {
        this.proxyService = proxyService;
        this.proxyConfig = proxyConfig;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public Mono<String> proxyRequest(
            HttpServletRequest request,
            @RequestParam String url,
            @RequestParam(required = false, defaultValue = "GET") String method,
            @RequestParam(required = false) MultiValueMap<String, String> formData,
            @RequestBody(required = false) String body) {
        
        String requestBody = body;
        if (formData != null && !formData.isEmpty()) {
            // Convert form data to URL-encoded string
            StringBuilder formBody = new StringBuilder();
            formData.forEach((key, values) -> {
                if (!"url".equals(key) && !"method".equals(key)) {
                    values.forEach(value -> {
                        if (formBody.length() > 0) {
                            formBody.append('&');
                        }
                        formBody.append(key).append('=').append(value);
                    });
                }
            });
            requestBody = formBody.toString();
        }

        return proxyService.proxyRequest(url, HttpMethod.valueOf(method), requestBody, request);
    }
}
