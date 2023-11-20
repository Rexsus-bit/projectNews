package com.main.mainserver.clientStats;


import com.stat.statserver.model.StatsRecordDto;
import com.stat.statserver.model.UserActivityView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class StatisticClient {

    @Value("${statistics.url}")
    private String url;

    private final RestTemplate restTemplate;

    public StatisticClient(@Qualifier("statsRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendStats(StatsRecordDto statsRecordDto) {
            HttpEntity<StatsRecordDto> entity = new HttpEntity<>(statsRecordDto);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + ("/stat/record"));
                    restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, StatsRecordDto.class);
    }

    public List<UserActivityView> getStats(List<Long> userIdList, LocalDateTime start, LocalDateTime end) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/stat/info");

        // Set request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Set request body
        // You need to create an object that represents the request body, assuming it's a JSON payload
        // For example, if you have a class RequestBodyClass with appropriate fields, use it here
        // RequestBodyClass requestBody = new RequestBodyClass(userIdList, start, end);

        // Assuming you're sending userIdList, start, and end as request parameters
        builder.queryParam("userIdList", userIdList);
        builder.queryParam("start", start);
        builder.queryParam("end", end);

        // Create the HttpEntity with headers and request body
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make the POST request
        ResponseEntity<List<UserActivityView>> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                // Specify the type of the response
                ParameterizedTypeReference.forType(List.class)
        );

        // Extract the response body
        List<UserActivityView> userActivityViews = responseEntity.getBody();

        return userActivityViews;
    }
    // TODO Есть ли делать обработку ошибок?

}
