package io.faroxy.controller;

import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.service.FaroxyProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for handling log-related operations.
 * Provides endpoints to retrieve request and response logs.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LogController {
    private final FaroxyProxyService faroxyProxyService;

    /**
     * Retrieves all request logs from the system.
     * @return List of RequestLog objects
     */
    @GetMapping("/requests")
    public List<RequestLog> getAllRequests() {
        return faroxyProxyService.getAllRequests();
    }

    /**
     * Retrieves all response logs from the system.
     * @return List of ResponseLog objects
     */
    @GetMapping("/responses")
    public List<ResponseLog> getAllResponses() {
        return faroxyProxyService.getAllResponses();
    }
}
