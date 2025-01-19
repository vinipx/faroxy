package io.faroxy.service;

import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.repository.RequestLogRepository;
import io.faroxy.repository.ResponseLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaroxyProxyService {
    private final WebClient webClient;
    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;

    public FaroxyProxyService(WebClient.Builder webClientBuilder,
                             RequestLogRepository requestLogRepository,
                             ResponseLogRepository responseLogRepository) {
        this.webClient = webClientBuilder.build();
        this.requestLogRepository = requestLogRepository;
        this.responseLogRepository = responseLogRepository;
    }

    public Mono<String> proxyRequest(String url, HttpMethod method, String body, HttpServletRequest request) {
        RequestLog requestLog = saveRequest(url, method, body, request);
        
        return webClient.method(method)
                .uri(url)
                .bodyValue(body != null ? body : "")
                .exchangeToMono(response -> {
                    saveResponse(response.statusCode().value(), Collections.emptyMap().toString(), "", requestLog.getId());
                    return response.bodyToMono(String.class);
                });
    }

    private RequestLog saveRequest(String url, HttpMethod method, String body, HttpServletRequest request) {
        RequestLog requestLog = RequestLog.builder()
                .method(method.name())
                .url(url)
                .headers(getHeadersAsString(request))
                .body(body)
                .timestamp(LocalDateTime.now())
                .build();
        return requestLogRepository.save(requestLog);
    }

    private void saveResponse(int statusCode, String headers, String body, Long requestId) {
        ResponseLog responseLog = ResponseLog.builder()
                .statusCode(statusCode)
                .headers(headers)
                .body(body)
                .timestamp(LocalDateTime.now())
                .requestId(requestId)
                .build();
        responseLogRepository.save(responseLog);
    }

    protected String getHeadersAsString(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return "";
        }

        return Collections.list(headerNames).stream()
                .map(name -> name + ": " + request.getHeader(name))
                .collect(Collectors.joining("\n"));
    }

    public List<RequestLog> getAllRequests() {
        return requestLogRepository.findAll();
    }

    public List<ResponseLog> getAllResponses() {
        return responseLogRepository.findAll();
    }
}
