package io.faroxy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for handling WebSocket messaging in the Faroxy application.
 * Responsible for sending proxy traffic updates to connected clients.
 */
@Service
public class WebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public WebSocketService(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a proxy traffic update to all connected WebSocket clients.
     *
     * @param request The HTTP request details
     * @param response The HTTP response details
     * @param responseTime The time taken to process the request in milliseconds
     * @param error Whether the request resulted in an error
     */
    public void sendTrafficUpdate(Map<String, Object> request, Map<String, Object> response, long responseTime, boolean error) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("request", request);
            message.put("response", response);
            message.put("responseTime", responseTime);
            message.put("error", error);

            String destination = "/topic/traffic";
            logger.debug("Sending WebSocket message to {}: {}", destination, message);
            
            messagingTemplate.convertAndSend(destination, message);
        } catch (Exception e) {
            logger.error("Error sending WebSocket message: {}", e.getMessage(), e);
        }
    }
}
