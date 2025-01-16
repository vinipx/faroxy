package io.faroxy.repository;

import io.faroxy.model.ResponseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseLogRepository extends JpaRepository<ResponseLog, Long> {
}
