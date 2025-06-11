package com.juan.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    // Bean para crear instancias WebClient específicas para servicios
    @Bean
    public WebClient inventoryServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://inventory-service").build();
    }

    @Bean
    public WebClient productServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://product-service").build();
    }
}
