package io.faroxy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for serving documentation pages.
 */
@Controller
@RequestMapping("/docs")
public class DocumentationController {

    /**
     * Serves the main documentation page.
     *
     * @return The documentation template name
     */
    @GetMapping
    public String getDocs() {
        return "documentation";
    }
}
