package com.main.mainserver.clientStats;

//import com.main.mainserver.exception.clientNewsApiExceptions.handler.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


@Configuration
public class StatsRestTemplateConfig {

    @Value("${STATS_SERVER_API_KEY}")
    private String API_KEY;

    @Bean("statsRestTemplate")
    public RestTemplate newsRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-API-KEY", API_KEY)
//                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

}
