package com.stat.statserver;

import com.stat.statserver.model.StatsRecord;
import com.stat.statserver.model.StatsRecordDto;
import com.stat.statserver.model.UserActivityView;
import com.stat.statserver.repository.StatsRecordJpaRepository;
import com.stat.statserver.service.NewsStatisticsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;

import static com.stat.statserver.util.Cleaner.cleanData;
import static com.stat.statserver.util.RandomDataGenerator.getRandomLongNumber;
import static com.stat.statserver.util.RandomDataGenerator.getRandomString;

@SpringBootTest
@Testcontainers
@Transactional
public class NewsStatisticsServiceTests {

    @Autowired
    private NewsStatisticsService newsStatisticsService;
    @Autowired
    private StatsRecordJpaRepository statsRecordJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres:14-alpine"));

    @DynamicPropertySource
    public static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    private static StatsRecordDto statsRecordDto;
    private static StatsRecord statsRecord;

    @BeforeAll
    static void initData() {
        statsRecordDto = new StatsRecordDto(getRandomLongNumber(), getRandomString(), LocalDateTime.now());
    }

    @BeforeEach
    void clean() {
        cleanData(jdbcTemplate);
    }

    @Test
    void shouldGetStatsTest() {
        newsStatisticsService.saveStatsRecord(statsRecordDto);
        List<UserActivityView> list = newsStatisticsService.getStats(List.of(statsRecordDto.getUserId()),
                statsRecordDto.getDateTime().minusDays(1),
                statsRecordDto.getDateTime().plusDays(1));

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(1, list.get(0).getHitsQuantity());
        Assertions.assertEquals(statsRecordDto.getUri(), list.get(0).getUri());
        Assertions.assertEquals(statsRecordDto.getUserId(), list.get(0).getUserId());
    }

    @Test
    void shouldSaveStatsRecordTest() {
        newsStatisticsService.saveStatsRecord(statsRecordDto);
        statsRecord = statsRecordJpaRepository.findById(1L).get();

        Assertions.assertEquals(statsRecordDto.getUserId(), statsRecord.getUserId());
        Assertions.assertEquals(statsRecordDto.getUri(),  statsRecord.getUri());
        Assertions.assertEquals(statsRecordDto.getDateTime(), statsRecord.getDateTime());
    }

}
