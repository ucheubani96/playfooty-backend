package com.playfooty.backendCore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditableConfig {
//    @Bean
//    public AuditorAware<String> auditorProvider() {
//        // Return currently authenticated user or service
//        return () -> Optional.of("system"); // Replace with actual user ID logic
//    }
}
