package io.faroxy.service;

import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.repository.RequestLogRepository;
import io.faroxy.repository.ResponseLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core service class for handling proxy operations.
 * Manages request/response logging, filtering, and proxy functionality.
 */
@Service
public class FaroxyProxyService {

    private final WebClient.Builder webClientBuilder;
    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructs a new FaroxyProxyService with required dependencies.
     * @param webClientBuilder WebClient builder for HTTP requests
     * @param requestLogRepository Repository for request logs
     * @param responseLogRepository Repository for response logs
     * @param messagingTemplate Messaging template for WebSocket operations
     */
    public FaroxyProxyService(WebClient.Builder webClientBuilder,
                             RequestLogRepository requestLogRepository,
                             ResponseLogRepository responseLogRepository,
                             SimpMessagingTemplate messagingTemplate) {
        this.webClientBuilder = webClientBuilder;
        this.requestLogRepository = requestLogRepository;
        this.responseLogRepository = responseLogRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Proxies an HTTP request to the target server.
     * @param url Target URL
     * @param method HTTP method
     * @param body Request body
     * @param request Original HTTP request
     * @return Response from target server
     */
    public Mono<String> proxyRequest(String url, HttpMethod method, String body, HttpServletRequest request) {
        // Log request
        RequestLog requestLog = new RequestLog();
        requestLog.setUrl(url);
        requestLog.setMethod(method.toString());
        requestLog.setHeaders(getHeadersAsString(request));
        requestLog.setBody(body);
        requestLog.setTimestamp(LocalDateTime.now());
        RequestLog savedRequest = requestLogRepository.save(requestLog);

        // Create WebClient request
        WebClient.RequestHeadersSpec<?> requestSpec = webClientBuilder.build()
                .method(method)
                .uri(url)
                .headers(headers -> {
                    Enumeration<String> headerNames = request.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        headers.add(name, request.getHeader(name));
                    }
                });

        if (body != null && !body.isEmpty()) {
            requestSpec = ((WebClient.RequestBodySpec) requestSpec).bodyValue(body);
        }

        return requestSpec.retrieve()
                .bodyToMono(String.class)
                .map(responseBody -> {
                    // Log response
                    ResponseLog responseLog = new ResponseLog();
                    responseLog.setRequestId(savedRequest.getId());
                    responseLog.setBody(responseBody);
                    responseLog.setTimestamp(LocalDateTime.now());
                    ResponseLog savedResponse = responseLogRepository.save(responseLog);

                    // Send WebSocket message
                    Map<String, Object> message = new HashMap<>();
                    message.put("request", savedRequest);
                    message.put("response", savedResponse);
                    message.put("timestamp", LocalDateTime.now());
                    messagingTemplate.convertAndSend("/topic/traffic", message);

                    return responseBody;
                });
    }

    /**
     * Converts request headers to a string representation.
     * @param request HTTP request to extract headers from
     * @return String representation of headers
     */
    protected String getHeadersAsString(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers.toString();
    }

    /**
     * Retrieves all request logs from the repository.
     * @return List of all request logs
     */
    public List<RequestLog> getAllRequests() {
        return requestLogRepository.findAll();
    }

    /**
     * Retrieves all response logs from the repository.
     * @return List of all response logs
     */
    public List<ResponseLog> getAllResponses() {
        return responseLogRepository.findAll();
    }

    /**
     * Retrieves all messages (requests and responses).
     * @return Combined list of all messages
     */
    public List<Object> getMessages() {
        List<RequestLog> requests = getAllRequests();
        List<ResponseLog> responses = getAllResponses();

        List<Map<String, Object>> messages = new ArrayList<>();
        
        // Add requests
        for (RequestLog request : requests) {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "request");
            message.put("content", request);
            message.put("timestamp", request.getTimestamp());
            messages.add(message);
        }
        
        // Add responses
        for (ResponseLog response : responses) {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "response");
            message.put("content", response);
            message.put("timestamp", response.getTimestamp());
            messages.add(message);
        }
        
        // Sort by timestamp descending
        messages.sort((a, b) -> ((LocalDateTime)b.get("timestamp")).compareTo((LocalDateTime)a.get("timestamp")));
        
        return new ArrayList<>(messages);
    }

    /**
     * Gets a filtered list of messages based on a search term.
     * @param filter Search term to filter messages
     * @return Filtered list of messages
     */
    public List<Object> getFilteredMessages(String filter) {
        return getMessages().stream()
                .filter(m -> ((Map<String, Object>)m).toString().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());
    }
}
