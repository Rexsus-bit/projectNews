package com.main.mainserver;

import com.main.mainserver.clientStats.StatisticClient;
import com.main.mainserver.exception.controllersExceptions.exceptions.NewsForApprovalException;
import com.main.mainserver.exception.controllersExceptions.exceptions.NewsIsNotAvaliableException;
import com.main.mainserver.exception.controllersExceptions.exceptions.RightsValidationException;
import com.main.mainserver.exception.controllersExceptions.exceptions.UniqueDataException;
import com.main.mainserver.exception.controllersExceptions.exceptions.UserIsNotFoundException;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsStatus;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.repository.NewsJpaRepository;
import com.main.mainserver.repository.UserJPARepository;
import com.main.mainserver.security.SecurityUser;
import com.main.mainserver.service.AdminService;
import com.main.mainserver.service.PublisherService;
import com.stat.statserver.model.UserActivityView;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.main.mainserver.security.SecurityUser.fromUser;
import static com.main.mainserver.util.Cleaner.cleanData;
import static com.main.mainserver.util.RandomDataGenerator.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


@SpringBootTest
@Testcontainers
@Transactional
public class AdminServiceTests {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private NewsJpaRepository newsJpaRepository;

    @MockBean
    private StatisticClient statisticClient;

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    UserActivityView mockUserActivityView;

    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres:14-alpine"));

    @DynamicPropertySource
    public static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    private static NewUserRequest newUserRequest;
    private static News news;

    @BeforeAll
    static void initData() {
        newUserRequest = new NewUserRequest(getRandomString(), getRandomString(), getRandomString());
        news = new News(null, getRandomString(), getRandomString(), getRandomString(), NewsStatus.CREATED,
                LocalDateTime.now(), null, null, null);
    }

    @BeforeEach
    void clean() {
        cleanData(jdbcTemplate);
    }

    @Test
    void shouldRejectNewsTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(user), mockHttpServletRequest);
        assertEquals(NewsStatus.CREATED, newsJpaRepository.findById(news.getId()).get().getNewsStatus());
        adminService.rejectNews(news.getId());
        assertEquals(NewsStatus.REJECTED, newsJpaRepository.findById(news.getId()).get().getNewsStatus());
    }

    @Test
    void shouldFailToRejectNewsTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(user), mockHttpServletRequest);
        assertEquals(NewsStatus.CREATED, newsJpaRepository.findById(news.getId()).get().getNewsStatus());
        adminService.rejectNews(news.getId());
        Assert.assertThrows(NewsForApprovalException.class, () -> adminService.rejectNews(news.getId()));
    }

    @Test
    void shouldApproveNewsTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(user), mockHttpServletRequest);
        assertEquals(NewsStatus.CREATED, newsJpaRepository.findById(news.getId()).get().getNewsStatus());
        adminService.approveNews(news.getId());
        assertEquals(NewsStatus.PUBLISHED, newsJpaRepository.findById(news.getId()).get().getNewsStatus());
    }

    @Test
    void shouldFailToApproveNewsTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(user), mockHttpServletRequest);
        assertEquals(NewsStatus.CREATED, newsJpaRepository.findById(news.getId()).get().getNewsStatus());
        adminService.approveNews(news.getId());
        Assert.assertThrows(NewsForApprovalException.class, () -> adminService.approveNews(news.getId()));
    }

    @Test
    public void shouldDeleteNewsTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(user), mockHttpServletRequest);
        assertEquals(news.getId(), newsJpaRepository.findById(news.getId()).get().getId());
        adminService.deleteNews(news.getId());
        Assertions.assertTrue(newsJpaRepository.findById(news.getId()).isEmpty());
    }

    @Test
    public void shouldFailToDeleteNewsTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        news = publisherService.createNews(news, (SecurityUser) fromUser(user), mockHttpServletRequest);
        assertEquals(news.getId(), newsJpaRepository.findById(news.getId()).get().getId());
        adminService.deleteNews(news.getId());
        Assert.assertThrows(NewsIsNotAvaliableException.class, () -> adminService.deleteNews(news.getId()));
    }

    @Test
    void shouldCreateUserTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        assertEquals(user, userJPARepository.findById(user.getId()).get());
    }

    @Test
    void shouldFailToCreateUserTest() {
        NewUserRequest newUserRequest = new NewUserRequest(null, null, null);
        Assert.assertThrows(IllegalArgumentException.class, () -> adminService.addUser(newUserRequest, Role.USER));
    }

    @Test
    void shouldFailToCreateUserWithAdminRoleTest() {
        Assert.assertThrows(RightsValidationException.class, () -> adminService.addUser(newUserRequest, Role.ADMIN));
    }

    @Test
    void shouldFailToCreateUserWithTheSameDetailsTest() {
        adminService.addUser(newUserRequest, Role.USER);
        Assert.assertThrows(UniqueDataException.class, () -> adminService.addUser(newUserRequest, Role.USER));
    }

    @Test
    void shouldDeleteUserTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        assertEquals(user, userJPARepository.findById(user.getId()).get());
        adminService.deleteUsers(user.getId());
        Assertions.assertTrue(userJPARepository.findById(user.getId()).isEmpty());
    }

    @Test
    void shouldFailToDeleteUserTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        assertEquals(user, userJPARepository.findById(user.getId()).get());
        adminService.deleteUsers(user.getId());
        Assert.assertThrows(UserIsNotFoundException.class, () -> adminService.deleteUsers(user.getId()));
    }

    @Test
    void shouldFailToDeleteUserWithRoleAdminTest() {
        User user = userJPARepository.save(new User(null, newUserRequest.getEmail(), newUserRequest.getPassword(),
                newUserRequest.getUsername(), Role.ADMIN, UserStatus.ACTIVE));
        assertEquals(user, userJPARepository.findById(user.getId()).get());
        Assert.assertThrows(RightsValidationException.class, () -> adminService.deleteUsers(user.getId()));
    }

    @Test
    void shouldBanUserTest() {
        User user = adminService.addUser(newUserRequest, Role.USER);
        assertEquals(UserStatus.ACTIVE, user.getUserStatus());
        adminService.banUser(user.getId(), UserStatus.BANNED);
        assertEquals(UserStatus.BANNED, userJPARepository.findById(user.getId()).get().getUserStatus());
    }

    @Test
    void shouldGetStatsTest() {
        when(statisticClient.getStats(anyList(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(mockUserActivityView));

        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now();
        List<UserActivityView> result = adminService.getStats(Arrays.asList(1L, 2L), start, end);

        Assertions.assertEquals(Collections.singletonList(mockUserActivityView), result);
    }

}
