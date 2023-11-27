package com.main.mainserver;

import com.main.mainserver.clientNewsApi.NewsApiRestClient;
import com.main.mainserver.exception.clientResponseExceptions.exceptions.ParameterIsNotIndicatedException;
import com.main.mainserver.model.newsApiDto.NewsReportDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@SpringBootTest(properties = {"news.api.url=http://127.0.0.1:8085"})
@AutoConfigureWireMock(port = 8085)
public class NewsApiRestClientTests {


    @Autowired
    private NewsApiRestClient newsApiRestClient;

    @Test
    public void ShouldReturnWeatherApiDtoTest() {
        NewsReportDto newsReportDto = newsApiRestClient.requestNews("tesla", LocalDate.now().minusDays(1), LocalDate.now());

        Assertions.assertEquals("ok", newsReportDto.getStatus());
        Assertions.assertEquals(3, newsReportDto.getTotalResults());
        Assertions.assertEquals(3, newsReportDto.getArticles().size());
        Assertions.assertEquals("Wired", newsReportDto.getArticles().get(0).getSourceDto().getName());
        Assertions.assertEquals("Morgan Meaker", newsReportDto.getArticles().get(0).getAuthor());
        Assertions.assertEquals("Tesla Is Suing Sweden", newsReportDto.getArticles().get(0).getTitle());
        Assertions.assertTrue(newsReportDto.getArticles().get(0).getDescription().startsWith("Unions are boycotting Tesla’s"));
        Assertions.assertTrue(newsReportDto.getArticles().get(0).getDescription().endsWith("Now Tesla has decided to sue."));

    }

    @Test()
    public void ShouldReturnWeatherApiErrorDtoTest() {
        ParameterIsNotIndicatedException exception = Assertions.assertThrows(ParameterIsNotIndicatedException.class
                , () -> newsApiRestClient.requestNews(null, LocalDate.now().minusDays(20), LocalDate.now()));
        Assertions.assertEquals("Произошла ошибка № 105. Для корректной работы необходимо указать параметры запроса.",
                exception.getMessage());
        Assertions.assertEquals(105, exception.getErrorCode());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

}
