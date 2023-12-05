package com.stat.statserver;

import com.stat.statserver.model.UserActivityView;
import com.stat.statserver.service.NewsStatisticsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.stat.statserver.util.RandomDataGenerator.getRandomLongNumber;
import static com.stat.statserver.util.RandomDataGenerator.getRandomString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsStatisticsControllerTests {

    @MockBean
    private NewsStatisticsService newsStatisticsService;
    @Autowired
    private MockMvc mvc;

    private static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static Long randLong;
    private static String randString;
    private static UserActivityView userActivityView;

    @Value("${STATS_SERVER_API_KEY}")
    private String password;

    @BeforeAll
    static void initData() {

        randLong = getRandomLongNumber();
        randString = getRandomString();

        userActivityView = new UserActivityView() {
            @Override
            public Long getUserId() {
                return randLong;
            }

            @Override
            public String getUri() {
                return randString;
            }

            @Override
            public Long getHitsQuantity() {
                return randLong;
            }
        };
    }


    @Test
    void shouldGetStatsTest() throws Exception {
        when(newsStatisticsService.getStats(anyList(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(userActivityView));

        mvc.perform(get("/stat/info")
                        .header("X-API-KEY", password)
                        .queryParam("userIdList", "1", "2")
                        .queryParam("start", LocalDateTime.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .queryParam("end", LocalDateTime.now().plusDays(1)
                                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", is(randLong), Long.class))
                .andExpect(jsonPath("$[0].uri", is(randString), String.class))
                .andExpect(jsonPath("$[0].hitsQuantity", is(randLong), Long.class));
    }

    @Test
    void shouldFailGetStatsWithoutApiKeyTest() throws Exception {
        when(newsStatisticsService.getStats(anyList(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(userActivityView));

        mvc.perform(get("/stat/info")
                        .queryParam("userIdList", "1", "2")
                        .queryParam("start", LocalDateTime.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .queryParam("end", LocalDateTime.now().plusDays(1)
                                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode", is(1002L), Long.class))
                .andExpect(jsonPath("$.message", is("Invalid API KEY"), String.class))
                .andExpect(jsonPath("$.status", is("UNAUTHORIZED"), String.class));
    }

    @Test
    void shouldFailGetStatsWithWrongRequestParameterTest() throws Exception {
        when(newsStatisticsService.getStats(anyList(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(userActivityView));

        mvc.perform(get("/stat/info")
                        .header("X-API-KEY", password)
                        .queryParam("userIdList",  "2t")
                        .queryParam("start", LocalDateTime.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .queryParam("end", LocalDateTime.now().plusDays(1)
                                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(977), Integer.class))
                .andExpect(jsonPath("$.message", is("Некорректный запрос"), String.class))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST"), String.class));
    }

    @Test
    void shouldFailGetStatsWithUnknownExceptionTest() throws Exception {
        when(newsStatisticsService.getStats(anyList(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenThrow(new RuntimeException());

        mvc.perform(get("/stat/info")
                        .header("X-API-KEY", password)
                        .queryParam("userIdList",  "2")
                        .queryParam("start", LocalDateTime.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .queryParam("end", LocalDateTime.now().plusDays(1)
                                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(9001), Integer.class))
                .andExpect(jsonPath("$.message",
                        is(String.format("Произошла ошибка № %d. Мы уже работаем над ее устранением.", 9001)),
                        String.class))
                .andExpect(jsonPath("$.status", is("INTERNAL_SERVER_ERROR"), String.class));
    }

}
