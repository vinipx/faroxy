package io.faroxy.repository;

import io.faroxy.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for RequestLog entities.
 * Provides CRUD operations for request logs.
 */
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}
