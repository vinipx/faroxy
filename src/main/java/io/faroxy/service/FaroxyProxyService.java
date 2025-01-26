package io.faroxy.service;

import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.repository.RequestLogRepository;
import io.faroxy.repository.ResponseLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core service class for handling proxy operations.
 * Manages request/response logging, filtering, and proxy functionality.
 */
@Service
public class FaroxyProxyService {

    private static final Logger log = LoggerFactory.getLogger(FaroxyProxyService.class);

    private final WebClient.Builder webClientBuilder;
    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public FaroxyProxyService(WebClient.Builder webClientBuilder,
                             RequestLogRepository requestLogRepository,
                             ResponseLogRepository responseLogRepository,
                             SimpMessagingTemplate messagingTemplate) {
        this.webClientBuilder = webClientBuilder;
        this.requestLogRepository = requestLogRepository;
        this.responseLogRepository = responseLogRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Mono<String> proxyRequest(String url, HttpMethod method, String body, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Validate and normalize URL
            URI uri = new URI(url);
            String normalizedUrl = uri.toString();
            
            // Log request
            RequestLog requestLog = new RequestLog();
            requestLog.setUrl(normalizedUrl);
            requestLog.setMethod(method.toString());
            requestLog.setHeaders(getHeadersAsString(request));
            requestLog.setBody(body);
            requestLog.setTimestamp(LocalDateTime.now());
            RequestLog savedRequest = requestLogRepository.save(requestLog);

            // Create WebClient request
            WebClient.RequestHeadersSpec<?> requestSpec = webClientBuilder.build()
                    .method(method)
                    .uri(uri)
                    .headers(headers -> {
                        Enumeration<String> headerNames = request.getHeaderNames();
                        while (headerNames.hasMoreElements()) {
                            String name = headerNames.nextElement();
                            if (!name.equalsIgnoreCase("host")) { // Skip host header
                                headers.add(name, request.getHeader(name));
                            }
                        }
                    });

            if (body != null && !body.isEmpty()) {
                requestSpec = ((WebClient.RequestBodySpec) requestSpec).bodyValue(body);
            }

            return requestSpec.retrieve()
                    .toEntity(String.class)
                    .map(responseEntity -> {
                        handleSuccessResponse(responseEntity, savedRequest, startTime);
                        return responseEntity.getBody();
                    })
                    .onErrorResume(error -> {
                        handleErrorResponse(error, savedRequest, startTime);
                        return Mono.just(getErrorMessage(error));
                    });
        } catch (URISyntaxException e) {
            log.error("Invalid URL format: {}", url, e);
            return Mono.just("Error: Invalid URL format - " + e.getMessage());
        }
    }

    private void handleSuccessResponse(ResponseEntity<String> responseEntity, RequestLog savedRequest, long startTime) {
        long responseTime = System.currentTimeMillis() - startTime;
        log.info("Response time: {}ms", responseTime);
        
        ResponseLog responseLog = new ResponseLog();
        responseLog.setRequestId(savedRequest.getId());
        responseLog.setStatusCode(responseEntity.getStatusCode().value());
        responseLog.setHeaders(responseEntity.getHeaders().toString());
        responseLog.setBody(responseEntity.getBody());
        responseLog.setTimestamp(LocalDateTime.now());
        responseLogRepository.save(responseLog);

        // Send WebSocket message
        Map<String, Object> message = createWebSocketMessage(savedRequest, responseLog, responseTime, false);
        messagingTemplate.convertAndSend("/topic/traffic", message);
    }

    private void handleErrorResponse(Throwable error, RequestLog savedRequest, long startTime) {
        long responseTime = System.currentTimeMillis() - startTime;
        log.error("Proxy request failed", error);

        ResponseLog responseLog = new ResponseLog();
        responseLog.setRequestId(savedRequest.getId());
        responseLog.setTimestamp(LocalDateTime.now());

        if (error instanceof WebClientResponseException) {
            WebClientResponseException wcError = (WebClientResponseException) error;
            responseLog.setStatusCode(wcError.getStatusCode().value());
            responseLog.setHeaders(wcError.getHeaders().toString());
            responseLog.setBody(wcError.getResponseBodyAsString());
        } else {
            responseLog.setStatusCode(HttpStatus.BAD_GATEWAY.value());
            responseLog.setBody(getErrorMessage(error));
        }

        responseLogRepository.save(responseLog);

        // Send WebSocket message
        Map<String, Object> message = createWebSocketMessage(savedRequest, responseLog, responseTime, true);
        messagingTemplate.convertAndSend("/topic/traffic", message);
    }

    private String getErrorMessage(Throwable error) {
        if (error instanceof ConnectException) {
            return "Connection refused: Unable to connect to the target server";
        } else if (error instanceof PrematureCloseException) {
            return "Connection closed prematurely by the target server";
        } else if (error instanceof WebClientResponseException) {
            return ((WebClientResponseException) error).getResponseBodyAsString();
        } else if (error instanceof URISyntaxException) {
            return "Invalid URL format: " + error.getMessage();
        }
        return error.getMessage();
    }

    private Map<String, Object> createWebSocketMessage(RequestLog request, ResponseLog response, long responseTime, boolean error) {
        Map<String, Object> message = new HashMap<>();
        
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", request.getMethod());
        requestMap.put("url", request.getUrl());
        requestMap.put("headers", request.getHeaders());
        requestMap.put("body", request.getBody());
        requestMap.put("timestamp", request.getTimestamp());
        
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("statusCode", response.getStatusCode());
        responseMap.put("headers", response.getHeaders());
        responseMap.put("body", response.getBody());
        
        message.put("request", requestMap);
        message.put("response", responseMap);
        message.put("responseTime", responseTime);
        message.put("error", error);
        
        return message;
    }

    private String getHeadersAsString(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
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
        List<RequestLog> requests = getAllRequests();
        List<ResponseLog> responses = getAllResponses();
        Map<Long, Map<String, Object>> messages = new LinkedHashMap<>();
        
        for (RequestLog request : requests) {
            Map<String, Object> message = new HashMap<>();
            message.put("request", request);
            messages.put(request.getId(), message);
        }
        
        for (ResponseLog response : responses) {
            Map<String, Object> message = messages.get(response.getRequestId());
            if (message != null) {
                message.put("response", response);
            }
        }
        
        return new ArrayList<>(messages.values());
    }

    public List<Object> getFilteredMessages(String filter) {
        return getMessages().stream()
                .filter(m -> ((Map<String, Object>)m).toString().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());
    }
}
