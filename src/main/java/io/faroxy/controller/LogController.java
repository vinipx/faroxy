package io.faroxy.controller;

import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.service.FaroxyProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LogController {
    private final FaroxyProxyService faroxyProxyService;

    @GetMapping("/requests")
    public List<RequestLog> getAllRequests() {
        return faroxyProxyService.getAllRequests();
    }

    @GetMapping("/responses")
    public List<ResponseLog> getAllResponses() {
        return faroxyProxyService.getAllResponses();
    }
}
