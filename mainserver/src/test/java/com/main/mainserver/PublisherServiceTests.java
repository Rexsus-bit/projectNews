package com.main.mainserver;

import com.main.mainserver.clientStats.StatisticClient;
import com.main.mainserver.exception.controllersExceptions.exceptions.RightsValidationException;
import com.main.mainserver.exception.controllersExceptions.exceptions.UserIsNotFoundException;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.model.news.NewsStatus;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.repository.NewsJpaRepository;
import com.main.mainserver.security.SecurityUser;
import com.main.mainserver.service.AdminService;
import com.main.mainserver.service.PublisherService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

import static com.main.mainserver.security.SecurityUser.fromUser;
import static com.main.mainserver.util.Cleaner.cleanData;
import static com.main.mainserver.util.RandomDataGenerator.getRandomEmail;
import static com.main.mainserver.util.RandomDataGenerator.getRandomLongNumber;
import static com.main.mainserver.util.RandomDataGenerator.getRandomString;

@SpringBootTest
@Testcontainers
@Transactional
public class PublisherServiceTests {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private NewsJpaRepository newsJpaRepository;

    @MockBean
    private StatisticClient statisticClient;
    @Mock
    private HttpServletRequest mockHttpServletRequest;
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

    private static NewUserRequest newUserRequest;
    private static NewUserRequest newUserRequest2;
    private static User publisherUser;
    private static User publisherUser2;
    private static News news;
    private static NewsRequestDto newsRequestDto;

    @BeforeAll
    static void initData() {
        newUserRequest = new NewUserRequest(getRandomEmail(), getRandomString(), getRandomString());
        newUserRequest2 = new NewUserRequest(getRandomEmail(), getRandomString(), getRandomString());
        news = new News(null, getRandomString(), getRandomString(), getRandomString(), NewsStatus.CREATED,
                LocalDateTime.now(), null, null, null);
        publisherUser = new User(getRandomLongNumber(), getRandomEmail(), getRandomString(), getRandomString(),
                Role.PUBLISHER, UserStatus.ACTIVE);
        publisherUser2 = new User(getRandomLongNumber(), getRandomEmail(), getRandomString(), getRandomString(),
                Role.PUBLISHER, UserStatus.ACTIVE);
        newsRequestDto = new NewsRequestDto(getRandomString(), getRandomString(), getRandomString());
    }

    @BeforeEach
    void clean() {
        cleanData(jdbcTemplate);
    }

    @Test
    public void shouldCreateNewsTest() {
        publisherUser = adminService.addUser(newUserRequest, Role.PUBLISHER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(publisherUser), mockHttpServletRequest);
        News result = newsJpaRepository.findById(news.getId()).get();
        Assertions.assertEquals(news, result);
    }

    @Test
    public void shouldFailToCreateNewsWithNoUserCreatedTest() {
        Assert.assertThrows(UserIsNotFoundException.class, () -> publisherService.createNews(news,
                (SecurityUser) fromUser(publisherUser), mockHttpServletRequest));
    }

    @Test
    public void shouldUpdateNewsTest() {
        publisherUser = adminService.addUser(newUserRequest, Role.PUBLISHER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(publisherUser), mockHttpServletRequest);
        News updatedNews = publisherService.updateNews(news.getId(), newsRequestDto, (SecurityUser) fromUser(publisherUser));
        Assertions.assertEquals(newsRequestDto.getDescription(), updatedNews.getDescription());
        Assertions.assertEquals(newsRequestDto.getText(), updatedNews.getText());
        Assertions.assertEquals(newsRequestDto.getTitle(), updatedNews.getTitle());
    }

    @Test
    public void shouldFailToUpdateNewsByWrongUserTest() {
        publisherUser = adminService.addUser(newUserRequest, Role.PUBLISHER);
        publisherUser2 = adminService.addUser(newUserRequest2, Role.PUBLISHER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(publisherUser), mockHttpServletRequest);
        Assert.assertThrows(RightsValidationException.class, () -> publisherService.
                updateNews(news.getId(), newsRequestDto, (SecurityUser) fromUser(publisherUser2)));
    }

    @Test
    public void shouldDeleteNewsTest() {
        publisherUser = adminService.addUser(newUserRequest, Role.PUBLISHER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(publisherUser), mockHttpServletRequest);
        Assertions.assertEquals(news, newsJpaRepository.findById(news.getId()).get());
        publisherService.deleteNews(news.getId(), (SecurityUser) fromUser(publisherUser));
        Assertions.assertTrue(newsJpaRepository.findById(news.getId()).isEmpty());
    }

    @Test
    public void shouldFailToDeleteNewsByWrongUserTest() {
        publisherUser = adminService.addUser(newUserRequest, Role.PUBLISHER);
        publisherUser2 = adminService.addUser(newUserRequest2, Role.PUBLISHER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(publisherUser), mockHttpServletRequest);
        Assertions.assertEquals(news, newsJpaRepository.findById(news.getId()).get());
        Assert.assertThrows(RightsValidationException.class, () -> publisherService
                .deleteNews(news.getId(), (SecurityUser) fromUser(publisherUser2)));
    }


}
