package com.main.mainserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.model.news.NewsStatus;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.service.PublisherService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;

import static com.main.mainserver.util.RandomDataGenerator.getRandomEmail;
import static com.main.mainserver.util.RandomDataGenerator.getRandomLocalDateTime;
import static com.main.mainserver.util.RandomDataGenerator.getRandomLongNumber;
import static com.main.mainserver.util.RandomDataGenerator.getRandomString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PublisherControllerTests {

    @MockBean
    private PublisherService publisherService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private static NewsRequestDto newsRequestDto;
    private static NewsRequestDto newsRequestDtoDefective;
    private static User user;
    private static News news;

    @BeforeAll
    static void initData() {
        user = new User(getRandomLongNumber(), getRandomEmail(), getRandomString(), getRandomString(),
                Role.ADMIN, UserStatus.ACTIVE);
        newsRequestDto = new NewsRequestDto(getRandomString(), getRandomString(), getRandomString());
        newsRequestDtoDefective = new NewsRequestDto("", "", getRandomString());
        news = new News(getRandomLongNumber(), getRandomString(), getRandomString(), getRandomString(),
                NewsStatus.CREATED, getRandomLocalDateTime(), user, new HashSet<>(), new ArrayList<>());
    }

    @Test
    void shouldCreateNewsTest() throws Exception {
        when(publisherService.createNews(any(News.class), any(), any(HttpServletRequest.class))).thenReturn(news);

        mvc.perform(post("/publish/news")
                        .content(mapper.writeValueAsString(newsRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(news.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(news.getTitle()), String.class))
                .andExpect(jsonPath("$.description", is(news.getDescription()), String.class))
                .andExpect(jsonPath("$.text", is(news.getText()), String.class))
                .andExpect(jsonPath("$.dateTime", is(news.getDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
                .andExpect(jsonPath("$.publisher.username", is(user.getUsername())))
                .andExpect(jsonPath("$.publisher.role", is(user.getRole().name())))
                .andExpect(jsonPath("$.publisher.userStatus", is(user.getUserStatus().name())))
                .andExpect(jsonPath("$.likesSet", hasSize(0)))
                .andExpect(jsonPath("$.commentsList", hasSize(0)));
    }

    @Test
    void shouldFailToCreateNewsTest() throws Exception {

        mvc.perform(post("/publish/news")
                        .content(mapper.writeValueAsString(newsRequestDtoDefective))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(177), Integer.class))
                .andExpect(jsonPath("$.messages.description", is("must not be blank"), String.class))
                .andExpect(jsonPath("$.messages.title", is("must not be blank"), String.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name()), String.class));
    }

    @Test
    void shouldUpdateNewsTest() throws Exception {
        when(publisherService.updateNews(anyLong(), any(NewsRequestDto.class), any())).thenReturn(news);

        mvc.perform(patch("/publish/news/{newsId}", news.getId())
                        .content(mapper.writeValueAsString(newsRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(news.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(news.getTitle()), String.class))
                .andExpect(jsonPath("$.description", is(news.getDescription()), String.class))
                .andExpect(jsonPath("$.text", is(news.getText()), String.class))
                .andExpect(jsonPath("$.dateTime", is(news.getDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
                .andExpect(jsonPath("$.publisher.username", is(user.getUsername())))
                .andExpect(jsonPath("$.publisher.role", is(user.getRole().name())))
                .andExpect(jsonPath("$.publisher.userStatus", is(user.getUserStatus().name())))
                .andExpect(jsonPath("$.likesSet", hasSize(0)))
                .andExpect(jsonPath("$.commentsList", hasSize(0)));
    }

    @Test
    void shouldDeleteNewsTest() throws Exception {
        mvc.perform(delete("/publish/news/delete/{newsId}", news.getId()))
                .andExpect(status().isOk());
    }


}
