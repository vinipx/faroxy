package io.faroxy.repository;

import io.faroxy.model.ResponseLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for ResponseLog entities.
 * Provides CRUD operations for response logs.
 */
public interface ResponseLogRepository extends JpaRepository<ResponseLog, Long> {
}
