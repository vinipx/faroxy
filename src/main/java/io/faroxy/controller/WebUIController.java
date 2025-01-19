package io.faroxy.controller;

import io.faroxy.service.FaroxyProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebUIController {

    @Autowired
    private FaroxyProxyService proxyService;

    @GetMapping("/ui")
    public String proxyUI(Model model) {
        model.addAttribute("messages", proxyService.getMessages());
        return "proxy";
    }

    @GetMapping("/api/messages")
    @ResponseBody
    public Object getMessages(@RequestParam(required = false) String filter) {
        if (filter != null && !filter.isEmpty()) {
            return proxyService.getFilteredMessages(filter);
        }
        return proxyService.getMessages();
    }
}
