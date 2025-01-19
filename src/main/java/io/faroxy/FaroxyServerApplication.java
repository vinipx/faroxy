package io.faroxy;

import io.faroxy.ui.ProxyTrafficUI;
import javafx.application.Application;
import javafx.application.Platform;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class FaroxyServerApplication {
    public static void main(String[] args) {
        // Set system property for JavaFX
        System.setProperty("javafx.macosx.embedded", "true");
        System.setProperty("apple.awt.UIElement", "true");
        
        // Launch JavaFX application
        Application.launch(ProxyTrafficUI.class, args);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
