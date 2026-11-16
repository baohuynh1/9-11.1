package com.bank.transaction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    /** RestTemplate trỏ sẵn tới account-service (VD: http://account-service:8080). */
    @Bean
    public RestTemplate accountRestTemplate(RestTemplateBuilder builder,
                                            @Value("${account-service.base-url}") String baseUrl) {
        return builder
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}
