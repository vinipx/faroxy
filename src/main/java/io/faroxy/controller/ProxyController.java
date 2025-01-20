package io.faroxy.controller;

import io.faroxy.config.ProxyConfiguration;
import io.faroxy.service.FaroxyProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST Controller for handling proxy requests.
 * Manages the forwarding of HTTP requests to target servers.
 */
@RestController
@RequestMapping("/proxy")
public class ProxyController {
    private final FaroxyProxyService proxyService;
    private final ProxyConfiguration proxyConfig;

    /**
     * Constructs a new ProxyController with required dependencies.
     * @param proxyService Service for proxy operations
     * @param proxyConfig Configuration for proxy settings
     */
    public ProxyController(FaroxyProxyService proxyService, ProxyConfiguration proxyConfig) {
        this.proxyService = proxyService;
        this.proxyConfig = proxyConfig;
    }

    /**
     * Handles incoming proxy requests and forwards them to target servers.
     * @param request Original HTTP request
     * @param url Target URL to proxy to
     * @param formData Form data to include in the request
     * @param body Request body
     * @return Response from target server
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public Mono<String> proxyRequest(
            HttpServletRequest request,
            @RequestParam String url,
            @RequestParam(required = false) MultiValueMap<String, String> formData,
            @RequestBody(required = false) String body) {
        
        String requestBody = body;
        if (formData != null && !formData.isEmpty()) {
            // Convert form data to URL-encoded string
            StringBuilder formBody = new StringBuilder();
            formData.forEach((key, values) -> {
                if (!"url".equals(key)) {
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

        return proxyService.proxyRequest(url, HttpMethod.valueOf(request.getMethod()), requestBody, request);
    }
}
