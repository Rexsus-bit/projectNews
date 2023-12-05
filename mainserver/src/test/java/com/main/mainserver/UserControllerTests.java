package com.main.mainserver;

import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsStatus;
import com.main.mainserver.model.newsApiDto.NewsApiDto;
import com.main.mainserver.model.newsApiDto.NewsReportDto;
import com.main.mainserver.model.newsApiDto.SourceDto;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.main.mainserver.util.RandomDataGenerator.getRandomEmail;
import static com.main.mainserver.util.RandomDataGenerator.getRandomLocalDateTime;
import static com.main.mainserver.util.RandomDataGenerator.getRandomLongNumber;
import static com.main.mainserver.util.RandomDataGenerator.getRandomNumberBetweenValues;
import static com.main.mainserver.util.RandomDataGenerator.getRandomString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;
    private static User user;
    private static News news1;
    private static News news2;
    private static News news3;
    private static Comment comment;
    private static NewsReportDto newsReportDto;
    private static String randomString;
    private static List<News> newsList;

    @BeforeAll
    static void initData() {
        user = new User(getRandomLongNumber(), getRandomEmail(), getRandomString(), getRandomString(), Role.ADMIN, UserStatus.ACTIVE);
        news1 = new News(getRandomLongNumber(), getRandomString(), getRandomString(), getRandomString(), NewsStatus.CREATED,
                getRandomLocalDateTime(), user, new HashSet<>(), new ArrayList<>());
        news2 = new News(getRandomLongNumber(), getRandomString(), getRandomString(), getRandomString(), NewsStatus.PUBLISHED,
                getRandomLocalDateTime(), user, Set.of(user), new ArrayList<>());
        news3 = new News(getRandomLongNumber(), getRandomString(), getRandomString(), getRandomString(), NewsStatus.PUBLISHED,
                getRandomLocalDateTime(), user, new HashSet<>(), new ArrayList<>());
        randomString = getRandomString();

        newsReportDto = new NewsReportDto(getRandomString(), (int) getRandomNumberBetweenValues(0, 1000),
                List.of(new NewsApiDto(new SourceDto(getRandomString()),
                        getRandomString(), getRandomString(), getRandomString(), getRandomString(),
                        LocalDateTime.now(), getRandomString())));
        comment = new Comment(getRandomLongNumber(), randomString, news1, user, getRandomLocalDateTime());
        newsList = List.of(news2, news3);
    }

    @Test
    void shouldFindNewsTest() throws Exception {
        when(userService.findNews(anyString(), anyList(), any(LocalDateTime.class), any(LocalDateTime.class),
                anyInt(), anyInt(), any(), any(HttpServletRequest.class))).thenReturn(newsList);

        mvc.perform(get("/user/news")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .queryParam("text", randomString)
                        .queryParam("usersIdList", "1", "2")
                        .queryParam("rangeStart", LocalDateTime.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .queryParam("rangeEnd", LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .queryParam("from", "0")
                        .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(news2.getId()), Long.class))
                .andExpect(jsonPath("$[0].title", is(news2.getTitle()), String.class))
                .andExpect(jsonPath("$[0].description", is(news2.getDescription()), String.class))
                .andExpect(jsonPath("$[0].text", is(news2.getText()), String.class))
                .andExpect(jsonPath("$[0].dateTime", is(news2.getDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
                .andExpect(jsonPath("$[0].publisher.username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].publisher.role", is(user.getRole().name())))
                .andExpect(jsonPath("$[0].publisher.userStatus", is(user.getUserStatus().name())))
                .andExpect(jsonPath("$[0].likesSet", hasSize(1)))
                .andExpect(jsonPath("$[0].commentsList", hasSize(0)))
                .andExpect(jsonPath("$[1].id", is(news3.getId()), Long.class))
                .andExpect(jsonPath("$[1].title", is(news3.getTitle()), String.class))
                .andExpect(jsonPath("$[1].description", is(news3.getDescription()), String.class))
                .andExpect(jsonPath("$[1].text", is(news3.getText()), String.class))
                .andExpect(jsonPath("$[1].dateTime", is(news3.getDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
                .andExpect(jsonPath("$[1].publisher.username", is(user.getUsername())))
                .andExpect(jsonPath("$[1].publisher.role", is(user.getRole().name())))
                .andExpect(jsonPath("$[1].publisher.userStatus", is(user.getUserStatus().name())))
                .andExpect(jsonPath("$[1].likesSet", hasSize(0)))
                .andExpect(jsonPath("$[1].commentsList", hasSize(0)));
    }

    @Test
    void shouldGetNewsFromWeatherApiServiceTest() throws Exception {
        when(userService.getNewsFromWeatherApiService(anyString(), any(), any(),
                any(), any(HttpServletRequest.class))).thenReturn(newsReportDto);

        mvc.perform(get("/user/news/external")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .queryParam("query", randomString)
                        .queryParam("from", LocalDate.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .queryParam("to", LocalDate.now()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(newsReportDto.getStatus()), String.class))
                .andExpect(jsonPath("$.totalResults", is(newsReportDto.getTotalResults()), Integer.class))
                .andExpect(jsonPath("$.articles", hasSize(newsReportDto.getArticles().size())))
                .andExpect(jsonPath("$.articles[0].source.name",
                        is(newsReportDto.getArticles().get(0).getSourceDto().getName()), String.class))
                .andExpect(jsonPath("$.articles[0].author",
                        is(newsReportDto.getArticles().get(0).getAuthor()), String.class))
                .andExpect(jsonPath("$.articles[0].title",
                        is(newsReportDto.getArticles().get(0).getTitle()), String.class))
                .andExpect(jsonPath("$.articles[0].description",
                        is(newsReportDto.getArticles().get(0).getDescription()), String.class))
                .andExpect(jsonPath("$.articles[0].url",
                        is(newsReportDto.getArticles().get(0).getUrl()), String.class))
                .andExpect(jsonPath("$.articles[0].publishedAt",
                        is(newsReportDto.getArticles().get(0).getPublishedAt()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))), String.class))
                .andExpect((jsonPath("$.articles[0].content",
                        is(newsReportDto.getArticles().get(0).getContent()), String.class)));
    }

    @Test
    void shouldAddCommentToNewsTest() throws Exception {
        when(userService.addCommentToNews(anyLong(), anyString(), any())).thenReturn(comment);

        mvc.perform(post("/user/news/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .queryParam("newsId", "1")
                        .queryParam("commentText", randomString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText()), String.class))
                .andExpect(jsonPath("$.owner.username", is(comment.getOwner().getUsername()), String.class))
                .andExpect(jsonPath("$.owner.role", is(comment.getOwner().getRole().name()), String.class))
                .andExpect(jsonPath("$.owner.userStatus", is(comment.getOwner()
                        .getUserStatus().name()), String.class))
                .andExpect(jsonPath("$.owner.userStatus", is(comment.getOwner()
                        .getUserStatus().name()), String.class))
                .andExpect(jsonPath("$.creationTime",
                        is(comment.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                        String.class));
    }

    @Test
    void shouldDeleteCommentToNewsTest() throws Exception {
        mvc.perform(delete("/user/news/comment/delete", news1.getId())
                .queryParam("commentId", comment.getId().toString())
        ).andExpect(status().isOk());
    }

    @Test
    void shouldAddLikeFromUserTest() throws Exception {
        mvc.perform(put("/user/{newsId}/like", news1.getId())).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteLikeFromUserTest() throws Exception {
        mvc.perform(delete("/user/{newsId}/like", news1.getId())).andExpect(status().isOk());
    }

    @Test
    void shouldGetTopNewsTest() throws Exception {
        when(userService.getTopNews(anyInt(), any(), any())).thenReturn(newsList);

        mvc.perform(get("/user/popular")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .queryParam("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(news2.getId()), Long.class))
                .andExpect(jsonPath("$[0].title", is(news2.getTitle()), String.class))
                .andExpect(jsonPath("$[0].description", is(news2.getDescription()), String.class))
                .andExpect(jsonPath("$[0].text", is(news2.getText()), String.class))
                .andExpect(jsonPath("$[0].dateTime", is(news2.getDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
                .andExpect(jsonPath("$[0].publisher.username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].publisher.role", is(user.getRole().name())))
                .andExpect(jsonPath("$[0].publisher.userStatus", is(user.getUserStatus().name())))
                .andExpect(jsonPath("$[0].likesSet", hasSize(1)))
                .andExpect(jsonPath("$[0].commentsList", hasSize(0)))
                .andExpect(jsonPath("$[1].id", is(news3.getId()), Long.class))
                .andExpect(jsonPath("$[1].title", is(news3.getTitle()), String.class))
                .andExpect(jsonPath("$[1].description", is(news3.getDescription()), String.class))
                .andExpect(jsonPath("$[1].text", is(news3.getText()), String.class))
                .andExpect(jsonPath("$[1].dateTime", is(news3.getDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
                .andExpect(jsonPath("$[1].publisher.username", is(user.getUsername())))
                .andExpect(jsonPath("$[1].publisher.role", is(user.getRole().name())))
                .andExpect(jsonPath("$[1].publisher.userStatus", is(user.getUserStatus().name())))
                .andExpect(jsonPath("$[1].likesSet", hasSize(0)))
                .andExpect(jsonPath("$[1].commentsList", hasSize(0)));
    }


}

