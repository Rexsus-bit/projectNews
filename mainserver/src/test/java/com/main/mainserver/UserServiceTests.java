package com.main.mainserver;

import com.main.mainserver.clientNewsApi.NewsApiRestClient;
import com.main.mainserver.clientStats.StatisticClient;
import com.main.mainserver.exception.controllersExceptions.exceptions.CommentIsNotExistedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.LikeIsExistedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.LikeIsNotExistedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.NewsIsNotAvaliableException;
import com.main.mainserver.exception.controllersExceptions.exceptions.NewsIsNotPublishedException;
import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsStatus;
import com.main.mainserver.model.newsApiDto.NewsReportDto;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.repository.CommentJPARepository;
import com.main.mainserver.repository.NewsJpaRepository;
import com.main.mainserver.repository.UserJPARepository;
import com.main.mainserver.security.SecurityUser;
import com.main.mainserver.service.PublisherService;
import com.main.mainserver.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.main.mainserver.security.SecurityUser.fromUser;
import static com.main.mainserver.util.Cleaner.cleanData;
import static com.main.mainserver.util.RandomDataGenerator.getRandomLongNumber;
import static com.main.mainserver.util.RandomDataGenerator.getRandomString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@Transactional
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private CommentJPARepository commentJPARepository;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private NewsJpaRepository newsJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private StatisticClient statisticClient;

    @MockBean
    private NewsApiRestClient newsApiRestClient;

    @Mock
    private NewsReportDto mockNewsReportDto;
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager em;


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

    private static User user1;
    private static User user2;
    private static NewUserRequest newUserRequest;
    private static News news1;
    private static News news2;
    private static News news3;
    private static News news4;
    @Mock
    private static HttpServletRequest mockHttpServletRequest;

    private static String randomString1;
    private static String randomString2;
    private static Long randomLong;

    @BeforeAll
    static void initData() {
        randomLong = getRandomLongNumber();
        randomString1 = getRandomString();
        randomString2 = getRandomString();
        user1 = new User(null, getRandomString(), getRandomString(), getRandomString(), Role.USER, UserStatus.ACTIVE);
        user2 = new User(null, getRandomString(), getRandomString(), getRandomString(), Role.USER, UserStatus.ACTIVE);
        newUserRequest = new NewUserRequest(getRandomString(), getRandomString(), getRandomString());
        news1 = new News(null, getRandomString(), getRandomString(), getRandomString(), NewsStatus.PUBLISHED,
                LocalDateTime.now(), user1, null, null);
        news2 = new News(null, getRandomString(), getRandomString(), getRandomString(), NewsStatus.PUBLISHED,
                LocalDateTime.now(), user1, null, null);
        news3 = new News(null, getRandomString(), getRandomString(), getRandomString(), NewsStatus.PUBLISHED,
                LocalDateTime.now(), user1, null, null);
        news4 = new News(null, getRandomString(), getRandomString(), getRandomString(), NewsStatus.CREATED,
                LocalDateTime.now(), user1, null, null);
    }

    @BeforeEach
    void clean() {
        cleanData(jdbcTemplate);
        cleanLinks();
    }

    private void cleanLinks() {
        news1.setCommentsList(null);
        news1.setLikesSet(null);
        news2.setCommentsList(null);
        news2.setLikesSet(null);
        news3.setCommentsList(null);
        news3.setLikesSet(null);
    }

    @Test
    void shouldFindNewsTest() {
        user1 = userJPARepository.save(user1);
        news1 = newsJpaRepository.save(news1);
        news2 = newsJpaRepository.save(news2);

        List<News> list = userService.findNews
                (null, Arrays.asList(user1.getId()), news1.getDateTime().minusHours(1),
                        news1.getDateTime().plusHours(1), 0, 10, (SecurityUser) fromUser(user1),
                        mockHttpServletRequest);

        Assertions.assertTrue(list.contains(news1));
        Assertions.assertTrue(list.contains(news2));

        Assertions.assertEquals(2, list.size());
        list = userService.findNews(news1.getTitle(), Arrays.asList(user1.getId()), news1.getDateTime().minusHours(1),
                news1.getDateTime().plusHours(1), 0, 10, (SecurityUser) fromUser(user1),
                mockHttpServletRequest);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void shouldGetNewsFromWeatherApiServiceTest() {
        user1 = userJPARepository.save(user1);
        when(newsApiRestClient.requestNews(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockNewsReportDto);
        LocalDate from = LocalDate.now().minusDays(5);
        LocalDate to = LocalDate.now();
        NewsReportDto result = userService.getNewsFromWeatherApiService(randomString1, from, to,
                (SecurityUser) fromUser(user1), mockHttpServletRequest);
        Assertions.assertEquals(mockNewsReportDto, result);
    }

    @Test
    void shouldAddCommentToNewsTest() {
        user1 = userJPARepository.save(user1);
        user2 = userJPARepository.save(user2);
        news1 = newsJpaRepository.save(news1);

        Comment comment = userService.addCommentToNews(news1.getId(), randomString1, (SecurityUser) fromUser(user2));

        em.detach(news1);
        em.detach(comment);
        news1 = newsJpaRepository.findById(news1.getId()).get();
        comment = commentJPARepository.findById(comment.getId()).get();

        Assertions.assertEquals(comment, news1.getCommentsList().get(0));
    }

    @Test
    void shouldFailToAddCommentToUnexistingNewsTest() {
        user1 = userJPARepository.save(user1);
        Assert.assertThrows(NewsIsNotAvaliableException.class, () -> userService
                .addCommentToNews(randomLong, randomString1, (SecurityUser) fromUser(user1)));
    }

    @Test
    void shouldFailToAddCommentToUnpublishedNewsTest() {
        user1 = userJPARepository.save(user1);
        news4 = publisherService.createNews(news4, (SecurityUser) fromUser(user1), mockHttpServletRequest);

        Assert.assertThrows(NewsIsNotPublishedException.class, () -> userService
                .addCommentToNews(news4.getId(), randomString1, (SecurityUser) fromUser(user1)));
    }

    @Test
    void shouldDeleteCommentTest() {
        user1 = userJPARepository.save(user1);
        user2 = userJPARepository.save(user2);
        news1 = newsJpaRepository.save(news1);
        em.detach(news1);
        Comment comment = userService.addCommentToNews(news1.getId(), randomString1, (SecurityUser) fromUser(user2));

        news1 = newsJpaRepository.findById(news1.getId()).get();
        Assertions.assertEquals(comment, news1.getCommentsList().get(0));
        userService.deleteCommentToNews(comment.getId(), (SecurityUser) fromUser(user2));
        Assertions.assertEquals(0, newsJpaRepository.findById(news1.getId()).get().getCommentsList().size());
    }

    @Test
    void shouldFailDeleteCommentTest() {
        user1 = userJPARepository.save(user1);
        Assert.assertThrows(CommentIsNotExistedException.class, () ->
                userService.deleteCommentToNews(1l, (SecurityUser) fromUser(user1)));

    }

    @Test
    void shouldAddLikeFromUserTest() {
        user1 = userJPARepository.save(user1);
        user2 = userJPARepository.save(user2);
        news1 = newsJpaRepository.save(news1); // TODO только тройка

        userService.addLikeFromUser(news1.getId(), (SecurityUser) fromUser(user2));
        em.detach(news1);
        News result = newsJpaRepository.findById(news1.getId()).get();
        Assertions.assertEquals(1, result.getLikesSet().size());
        Assertions.assertTrue(result.getLikesSet().contains(user2));
    }

    @Test
    void shouldFailToAddLikeFromUserTwiceTest() {
        user1 = userJPARepository.save(user1);
        user2 = userJPARepository.save(user2);
        news1 = newsJpaRepository.save(news1);

        userService.addLikeFromUser(news1.getId(), (SecurityUser) fromUser(user2));
        Assert.assertThrows(LikeIsExistedException.class, () ->
                userService.addLikeFromUser(news1.getId(), (SecurityUser) fromUser(user2)));
    }

    @Test
    void shouldFailToAddLikeUnexistingNewsTest() {
        user1 = userJPARepository.save(user1);
        Assert.assertThrows(NewsIsNotAvaliableException.class, () ->
                userService.addLikeFromUser(news1.getId(), (SecurityUser) fromUser(user2)));
    }

    @Test
    void shouldDeleteLikeFromUserTest() {
        user1 = userJPARepository.save(user1);
        user2 = userJPARepository.save(user2);
        news1 = newsJpaRepository.save(news1);

        userService.addLikeFromUser(news1.getId(), (SecurityUser) fromUser(user2));
        em.detach(news1);
        News news1 = newsJpaRepository.findById(UserServiceTests.news1.getId()).get();
        Assertions.assertEquals(1, news1.getLikesSet().size());
        Assertions.assertTrue(news1.getLikesSet().contains(user2));
        userService.deleteLikeFromUser(UserServiceTests.news1.getId(), (SecurityUser) fromUser(user2));
        news1 = newsJpaRepository.findById(UserServiceTests.news1.getId()).get();
        Assertions.assertEquals(0, news1.getLikesSet().size());
    }

    @Test
    void shouldFailToDeleteLikeFromUserTest() {
        Assert.assertThrows(LikeIsNotExistedException.class, () -> userService
                .deleteLikeFromUser(UserServiceTests.news1.getId(), (SecurityUser) fromUser(user2)));
    }

    @Test
    void shouldGetTopNewsTest() {
        user1 = userJPARepository.save(user1);
        news1 = newsJpaRepository.save(news1);
        news2 = newsJpaRepository.save(news2);
        news3 = newsJpaRepository.save(news3);

        userService.addCommentToNews(news2.getId(), randomString1, (SecurityUser) fromUser(user1));
        userService.addCommentToNews(news2.getId(), randomString1, (SecurityUser) fromUser(user1));
        userService.addCommentToNews(news3.getId(), randomString1, (SecurityUser) fromUser(user1));

        List<News> list = userService.getTopNews(10, (SecurityUser) fromUser(user1), mockHttpServletRequest);

        Assertions.assertEquals(news2, list.get(0));
        Assertions.assertEquals(news3, list.get(1));
        Assertions.assertEquals(news1, list.get(2));
    }

}


