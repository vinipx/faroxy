package io.faroxy.controller;

import io.faroxy.service.FaroxyProxyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling web UI related requests.
 * Manages the proxy UI and message filtering functionality.
 */
@Controller
public class WebUIController {
    
    private final FaroxyProxyService proxyService;

    /**
     * Constructs a new WebUIController with the proxy service.
     * @param proxyService Service for proxy operations
     */
    public WebUIController(FaroxyProxyService proxyService) {
        this.proxyService = proxyService;
    }

    /**
     * Renders the main proxy UI page.
     * @param model Spring MVC model
     * @return View name for the proxy UI
     */
    @GetMapping("/ui")
    public String proxyUI(Model model) {
        return "proxy";
    }

    /**
     * Retrieves filtered messages based on search criteria.
     * @param filter Optional filter string
     * @return Filtered list of messages
     */
    @GetMapping("/api/messages")
    @ResponseBody
    public Object getMessages(@RequestParam(required = false) String filter) {
        if (filter != null && !filter.isEmpty()) {
            return proxyService.getFilteredMessages(filter);
        }
        return proxyService.getMessages();
    }
}
