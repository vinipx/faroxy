package io.faroxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Main application class for the Faroxy Server.
 * Initializes the Spring Boot application and configures web client settings.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class FaroxyServerApplication {
    
    /**
     * Main entry point for the application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FaroxyServerApplication.class, args);
    }
}
