package io.faroxy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity class representing a response log entry.
 * Stores information about HTTP responses received from target servers.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long requestId;
    private Integer statusCode;
    
    @Column(columnDefinition = "TEXT")
    private String headers;
    
    @Column(columnDefinition = "TEXT")
    private String body;
    
    private LocalDateTime timestamp;
}
