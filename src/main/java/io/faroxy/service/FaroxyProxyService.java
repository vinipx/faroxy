package io.faroxy.service;

import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.repository.RequestLogRepository;
import io.faroxy.repository.ResponseLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Service
public class FaroxyProxyService {
    private final WebClient webClient;
    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;
    private final Scheduler jdbcScheduler;

    public FaroxyProxyService(WebClient.Builder webClientBuilder,
                             RequestLogRepository requestLogRepository,
                             ResponseLogRepository responseLogRepository,
                             Scheduler jdbcScheduler) {
        this.webClient = webClientBuilder.build();
        this.requestLogRepository = requestLogRepository;
        this.responseLogRepository = responseLogRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    public Mono<String> proxyRequest(String url, HttpMethod method, String body, HttpServletRequest request) {
        RequestLog requestLog = saveRequest(url, method, body, request);
        
        WebClient.RequestHeadersSpec<?> spec = webClient.method(method)
                .uri(url);

        // Forward all headers from the original request
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (!"host".equalsIgnoreCase(headerName) && !"content-length".equalsIgnoreCase(headerName)) {
                spec = ((WebClient.RequestHeadersSpec<?>) spec).header(headerName, headerValue);
            }
        }

        // Handle request body
        if (body != null && !body.isEmpty()) {
            if (request.getContentType() != null && request.getContentType().contains("application/x-www-form-urlencoded")) {
                spec = ((WebClient.RequestHeadersSpec<?>) spec).header(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                // Convert the form data to the correct format
                spec = ((WebClient.RequestBodySpec) spec).bodyValue(body);
            } else {
                spec = ((WebClient.RequestBodySpec) spec).bodyValue(body);
            }
        }

        return spec.exchangeToMono(response -> 
            response.bodyToMono(String.class)
                .flatMap(responseBody -> {
                    String headers = response.headers().asHttpHeaders().toString();
                    return Mono.fromRunnable(() -> 
                        saveResponse(response.statusCode().value(), headers, responseBody, requestLog.getId())
                    )
                    .subscribeOn(jdbcScheduler)
                    .then(Mono.just(responseBody));
                })
        );
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

    public List<Object> getMessages() {
        List<Object> messages = new ArrayList<>();
        messages.addAll(requestLogRepository.findAll().stream()
            .map(req -> new MessageDTO("REQUEST", req.getTimestamp(), formatRequest(req)))
            .collect(Collectors.toList()));
        messages.addAll(responseLogRepository.findAll().stream()
            .map(resp -> new MessageDTO("RESPONSE", resp.getTimestamp(), formatResponse(resp)))
            .collect(Collectors.toList()));
        messages.sort((a, b) -> ((MessageDTO)b).timestamp().compareTo(((MessageDTO)a).timestamp()));
        return messages;
    }

    public List<Object> getFilteredMessages(String filter) {
        return getMessages().stream()
            .filter(msg -> ((MessageDTO)msg).content().toLowerCase().contains(filter.toLowerCase()))
            .collect(Collectors.toList());
    }

    private String formatRequest(RequestLog req) {
        return String.format("Method: %s\nURL: %s\nHeaders: %s\nBody: %s",
            req.getMethod(),
            req.getUrl(),
            req.getHeaders(),
            req.getBody());
    }

    private String formatResponse(ResponseLog resp) {
        return String.format("Status: %d\nHeaders: %s\nBody: %s",
            resp.getStatusCode(),
            resp.getHeaders(),
            resp.getBody());
    }

    private record MessageDTO(String type, LocalDateTime timestamp, String content) {}
}
