package com.main.mainserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsStatus;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.service.AdminService;
import com.stat.statserver.model.UserActivityView;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.main.mainserver.util.RandomDataGenerator.getRandomEmail;
import static com.main.mainserver.util.RandomDataGenerator.getRandomLongNumber;
import static com.main.mainserver.util.RandomDataGenerator.getRandomString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTests {

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;
    private static NewUserRequest newUserRequest;
    private static User user;
    private static News news;
    private static Long randLong;
    private static String randString;

    @BeforeAll
    static void initData() {
        newUserRequest = new NewUserRequest(getRandomEmail(), getRandomString(), getRandomString());
        user = new User(getRandomLongNumber(), getRandomEmail(), getRandomString(), getRandomString(), Role.ADMIN,
                UserStatus.ACTIVE);
        news = new News(getRandomLongNumber(), getRandomString(), getRandomString(), getRandomString(),
                NewsStatus.CREATED, LocalDateTime.now(), null, null, null);
        randLong = getRandomLongNumber();
        randString = getRandomString();
    }

    @Test
    void shouldCreateUserTest() throws Exception {
        when(adminService.addUser(any(NewUserRequest.class), any(Role.class))).thenReturn(user);

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(newUserRequest))
                        .queryParam("role", Role.USER.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail()), String.class))
                .andExpect(jsonPath("$.username", is(user.getUsername()), String.class))
                .andExpect(jsonPath("$.role", is(user.getRole().name()), String.class))
                .andExpect(jsonPath("$.userStatus", is(user.getUserStatus().name()), String.class));
    }

    @Test
    void shouldGetStatsTest() throws Exception {
        when(adminService.getStats(anyList(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(new UserActivityView() {
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
                }));

        mvc.perform(get("/admin/stats")
                        .queryParam("userIdList", "1", "2")
                        .queryParam("start", LocalDateTime.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .queryParam("end", LocalDateTime.now().minusDays(1)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", is(randLong), Long.class))
                .andExpect(jsonPath("$[0].uri", is(randString), String.class))
                .andExpect(jsonPath("$[0].hitsQuantity", is(randLong), Long.class));
    }

    @Test
    void shouldBanUserTest() throws Exception {
        mvc.perform(patch("/admin/user/{userId}/ban", user.getId())
                        .queryParam("userStatus", UserStatus.BANNED.name()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailToBanUserTest() throws Exception {
        mvc.perform(patch("/admin/user/{userId}/ban", user.getId())
                        .queryParam("userStatus", "1L"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(357), Integer.class))
                .andExpect(jsonPath("$.messages.message", is("Введены некорректные данные"), String.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name()), String.class));
    }

    @Test
    void shouldRejectNewsTest() throws Exception {
        mvc.perform(patch("/admin/news/{newsId}/reject", news.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldApproveNewsTest() throws Exception {
        mvc.perform(patch("/admin/news/{newsId}/publish", news.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteNewsTest() throws Exception {
        mvc.perform(delete("/admin/news/{newsId}/delete", news.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserTest() throws Exception {
        mvc.perform(delete("/admin/users/{userId}", user.getId()))
                .andExpect(status().isOk());
    }

}
