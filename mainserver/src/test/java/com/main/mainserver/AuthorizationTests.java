package com.main.mainserver;

import com.main.mainserver.clientStats.StatisticClient;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.security.SecurityUser;
import com.main.mainserver.service.AdminService;
import com.main.mainserver.service.PublisherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;

import static com.main.mainserver.util.Cleaner.cleanData;
import static com.main.mainserver.util.RandomDataGenerator.getRandomEmail;
import static com.main.mainserver.util.RandomDataGenerator.getRandomString;
import static com.main.mainserver.util.RequestPostProcessorGenerator.getRequestPostProcessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AuthorizationTests {

    @Autowired
    private AdminService adminService;
    @MockBean
    private PublisherService publisherService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @MockBean
    private StatisticClient statisticClient;

    private static NewUserRequest newUserRequest;
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

    @BeforeAll
    static void initData() {
        newUserRequest = new NewUserRequest(getRandomEmail(), getRandomString(), getRandomString());
    }

    @BeforeEach
    void clean() {
        cleanData(jdbcTemplate);
    }

    @Test
    void shouldUserBeAuthorizedTest() throws Exception {
        adminService.addUser(newUserRequest, Role.USER);

        mvc.perform(get("/user/news")
                        .with(getRequestPostProcessor(newUserRequest.getEmail(), newUserRequest.getPassword()))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUserFailToBeAuthorizedTest() throws Exception {
        mvc.perform(get("/user/news")
                        .with(getRequestPostProcessor(newUserRequest.getEmail(), newUserRequest.getPassword()))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUserBeForbiddenToDeleteNewsTest() throws Exception {
        adminService.addUser(newUserRequest, Role.USER);
        Mockito.doNothing().when(publisherService).deleteNews(anyLong(), any(SecurityUser.class));

        mvc.perform(delete("/publish/news/delete/{newsId}", 1)
                        .with(getRequestPostProcessor(newUserRequest.getEmail(), newUserRequest.getPassword())))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldPublisherBeAuthorizedToDeleteNewsTest() throws Exception {
        adminService.addUser(newUserRequest, Role.PUBLISHER);
        Mockito.doNothing().when(publisherService).deleteNews(anyLong(), any(SecurityUser.class));

        mvc.perform(delete("/publish/news/delete/{newsId}", 1)
                        .with(getRequestPostProcessor(newUserRequest.getEmail(), newUserRequest.getPassword())))
                .andExpect(status().isOk());
    }


}
