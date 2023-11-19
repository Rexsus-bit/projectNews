package com.main.mainserver.clientStats;


import com.stat.statserver.model.StatsRecordDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class StatisticClient {

    @Value("${statistics.url}")
    private String url;

    @Value("${STATS_SERVER_API_KEY}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    public StatisticClient(@Qualifier("statsRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendStatisticsInfo(StatsRecordDto statsRecordDto) {
            HttpHeaders headers = new HttpHeaders(); // TODO надо?
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add("X-API-KEY", API_KEY);
            HttpEntity<StatsRecordDto> entity = new HttpEntity<>(statsRecordDto, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + ("/stat/record"));
                    restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, StatsRecordDto.class);
    }
    // TODO Есть ли делать обработку ошибок?

}
