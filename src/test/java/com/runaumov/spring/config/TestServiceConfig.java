package com.runaumov.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runaumov.spring.api.WeatherApiClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {
        "com.runaumov.spring.service",
        "com.runaumov.spring.dao"
})
@EnableTransactionManagement
public class TestServiceConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}