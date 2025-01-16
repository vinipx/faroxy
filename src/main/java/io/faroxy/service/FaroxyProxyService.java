package io.faroxy.service;

import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.repository.RequestLogRepository;
import io.faroxy.repository.ResponseLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaroxyProxyService {
    private static final Logger logger = LoggerFactory.getLogger(FaroxyProxyService.class);
    private final WebClient.Builder webClientBuilder;
    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;

    public Mono<String> proxyRequest(String url, HttpMethod method, String body, HttpServletRequest request) {
        logger.info("Proxying request: {} {} with body length: {}", method, url, body.length());
        RequestLog requestLog = saveRequest(url, method, body, request);

        return webClientBuilder.build()
                .method(method)
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    logger.info("Received response for request: {} {}", method, url);
                    saveResponse(response, requestLog.getId());
                })
                .doOnError(error -> logger.error("Error proxying request: {} {}", method, url, error));
    }

    private RequestLog saveRequest(String url, HttpMethod method, String body, HttpServletRequest request) {
        logger.debug("Saving request: {} {}", method, url);
        RequestLog requestLog = RequestLog.builder()
                .method(method.name())
                .url(url)
                .body(body)
                .headers(getHeadersAsString(request))
                .timestamp(LocalDateTime.now())
                .build();
        return requestLogRepository.save(requestLog);
    }

    private void saveResponse(String response, Long requestId) {
        logger.debug("Saving response for request ID: {}", requestId);
        ResponseLog responseLog = ResponseLog.builder()
                .statusCode(200)
                .body(response)
                .timestamp(LocalDateTime.now())
                .requestId(requestId)
                .build();
        responseLogRepository.save(responseLog);
    }

    private String getHeadersAsString(HttpServletRequest request) {
        return java.util.Collections.list(request.getHeaderNames()).stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .collect(Collectors.joining(", "));
    }

    public List<RequestLog> getAllRequests() {
        logger.info("Retrieving all requests");
        return requestLogRepository.findAll();
    }

    public List<ResponseLog> getAllResponses() {
        logger.info("Retrieving all responses");
        return responseLogRepository.findAll();
    }
}
