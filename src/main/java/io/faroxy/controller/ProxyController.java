package io.faroxy.controller;

import io.faroxy.service.FaroxyProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/proxy")
public class ProxyController {
    private final FaroxyProxyService proxyService;

    public ProxyController(FaroxyProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public Mono<String> proxyRequest(
            HttpServletRequest request,
            @RequestParam String url,
            @RequestParam(required = false, defaultValue = "GET") String method,
            @RequestBody(required = false) String body) {
        return proxyService.proxyRequest(url, HttpMethod.valueOf(method), body, request);
    }
}
