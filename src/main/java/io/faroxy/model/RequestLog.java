package io.faroxy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity class representing a request log entry.
 * Stores information about HTTP requests processed by the proxy.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String method;
    private String url;
    
    @Column(columnDefinition = "TEXT")
    private String headers;
    
    @Column(columnDefinition = "TEXT")
    private String body;
    
    private LocalDateTime timestamp;
}
