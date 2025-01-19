package io.faroxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "faroxy.proxy")
public class ProxyConfiguration {
    
    /**
     * The port number that the proxy server will listen on.
     * Can be overridden using the FAROXY_PROXY_PORT environment variable
     * or the faroxy.proxy.port system property.
     */
    private int port = 8888; // Default value
}
