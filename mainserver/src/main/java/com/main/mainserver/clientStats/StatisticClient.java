package com.main.mainserver.clientStats;


import com.stat.statserver.model.StatsRecordDto;
import com.stat.statserver.model.UserActivityView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class StatisticClient {

    @Value("${statistics.url}")
    private String url;
    private static final String TOPIC = "statsTopic";
    private final RestTemplate restTemplate;
    private final KafkaTemplate<Long, StatsRecordDto> kafkaTemplate;

    public StatisticClient(@Qualifier("statsRestTemplate") RestTemplate restTemplate,
                           KafkaTemplate<Long, StatsRecordDto> kafkaTemplate) {
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStats(StatsRecordDto statsRecordDto) {
        kafkaTemplate.send(TOPIC, statsRecordDto.getUserId(), statsRecordDto);
    }

    public List<UserActivityView> getStats(List<Long> userIdList, LocalDateTime start, LocalDateTime end) {
        HttpHeaders headers = new HttpHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/stat/info");
        builder.queryParam("userIdList", userIdList);
        builder.queryParam("start", start);
        builder.queryParam("end", end);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<UserActivityView>> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                ParameterizedTypeReference.forType(List.class)
        );

        return responseEntity.getBody();
    }

}
