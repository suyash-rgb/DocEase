// src/main/java/com/docease/aiservice/config/WebClientConfig.java
package com.docease.aiservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // For external APIs (Gemini, etc) — NO @LoadBalanced
    @Bean("externalWebClientBuilder")
    public WebClient.Builder externalWebClientBuilder() {
        return WebClient.builder();
    }

    // For internal microservices only — WITH @LoadBalanced
    @Bean("internalWebClientBuilder")
    @LoadBalanced
    public WebClient.Builder internalWebClientBuilder() {
        return WebClient.builder();
    }
}