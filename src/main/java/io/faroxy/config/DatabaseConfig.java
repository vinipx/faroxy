package io.faroxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Configuration class for database-related settings.
 * Provides beans for database operations and scheduling.
 */
@Configuration
public class DatabaseConfig {
    
    /**
     * Creates a scheduler for JDBC operations.
     * @return Scheduler instance configured for database operations
     */
    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.boundedElastic();
    }
}
