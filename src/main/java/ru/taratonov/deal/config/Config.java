package ru.taratonov.deal.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.taratonov.deal.util.DealResponseErrorHandler;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new DealResponseErrorHandler())
                .build();
    }
}