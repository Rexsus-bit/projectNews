package com.main.mainserver.clientNewsApi;

import com.main.mainserver.exception.clientNewsApiExceptions.handler.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


@Configuration
public class NewsRestTemplateConfig {

    @Value("${NEWS_API_KEY}")
    private String API_KEY;

    @Bean("newsRestTemplate")
    public RestTemplate newsRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Api-Key", API_KEY)
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

}
