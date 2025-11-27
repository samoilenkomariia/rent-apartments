package org.example.rentapartment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

@Configuration
public class LabConfig {
    @Bean
    @Scope("prototype")
    public String requestTracker() {
        return "Request-ID-" + UUID.randomUUID().toString().substring(0, 8);
    }
}