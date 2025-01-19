package io.faroxy;

import io.faroxy.ui.ProxyTrafficUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FaroxyServerApplication {
    
    public static void main(String[] args) {
        // Check if we should start in web-only mode
        boolean webOnly = Boolean.getBoolean("web.only");
        
        if (webOnly) {
            // Start Spring Boot application without JavaFX
            SpringApplication.run(FaroxyServerApplication.class, args);
        } else {
            // Start both Spring Boot and JavaFX
            try {
                // Set system property for JavaFX
                System.setProperty("javafx.macosx.embedded", "true");
                System.setProperty("apple.awt.UIElement", "true");
                
                // Launch JavaFX application
                ProxyTrafficUI.launch(ProxyTrafficUI.class, args);
            } catch (Exception e) {
                System.err.println("Failed to start JavaFX UI, falling back to web-only mode");
                SpringApplication.run(FaroxyServerApplication.class, args);
            }
        }
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
