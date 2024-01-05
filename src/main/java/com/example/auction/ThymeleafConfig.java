package com.example.auction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {

    @Bean
    public ThymeleafUtil thymeleafUtil() {
        return new ThymeleafUtil();
    }
}
