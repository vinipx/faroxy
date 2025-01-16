package io.faroxy.controller;

import io.faroxy.service.FaroxyProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
public class ProxyController {
    private final FaroxyProxyService faroxyProxyService;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public Mono<ResponseEntity<String>> proxyRequest(
            @RequestBody(required = false) String body,
            HttpServletRequest request,
            @RequestParam String targetUrl) {
        
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        return faroxyProxyService.proxyRequest(targetUrl, method, body, request)
                .map(ResponseEntity::ok);
    }
}
