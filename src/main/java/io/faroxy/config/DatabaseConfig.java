package io.faroxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class DatabaseConfig {
    
    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.boundedElastic();
    }
}
