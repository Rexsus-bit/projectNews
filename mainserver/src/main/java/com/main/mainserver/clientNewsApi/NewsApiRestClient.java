package com.main.mainserver.clientNewsApi;

import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.InvalidRequestTimePeriodException;
import com.main.mainserver.model.newsApiDto.NewsReportDto;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;


@Service
public class NewsApiRestClient {

    @Value("${news.api.url}")
    private String url;

    private final RestTemplate restTemplate;
    private final RateLimiter newsRateLimiter;

    public NewsApiRestClient(@Qualifier("newsRestTemplate") RestTemplate restTemplate,
                             @Qualifier("newsLimiter") RateLimiter rateLimiter) {
        this.newsRateLimiter = rateLimiter;
        this.restTemplate = restTemplate;
    }

    public NewsReportDto requestNews(String query, LocalDate from, LocalDate to) {
        LocalDate restrictionDate = LocalDate.now().minusMonths(1);
        if (from.isBefore(restrictionDate)) {
            throw new InvalidRequestTimePeriodException(109, from, restrictionDate);
        }
        return RateLimiter.decorateSupplier(newsRateLimiter, () -> {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + ("/v2/everything"))
                    .queryParam("q", query)
                    .queryParam("language", "en")
                    .queryParam("from", from)
                    .queryParam("to", to);
//                    .queryParam("sortBy", query); // TODO добавить если будет время

        ResponseEntity<NewsReportDto> newsApiDtoResponseEntity =
                restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, NewsReportDto.class);
        return newsApiDtoResponseEntity.getBody();
    }).get();

    }
}