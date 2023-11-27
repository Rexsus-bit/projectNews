package com.main.mainserver.controller;


import com.main.mainserver.exception.controllersExceptions.handler.ApiError;
import com.main.mainserver.mapper.UserMapper;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.UserFullDto;

import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.service.AdminService;
import com.stat.statserver.model.UserActivityView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
@Tag(name = "Admin Controller", description = "Доступен только администраторам")
public class AdminController {

    private final AdminService adminService;
    private final UserMapper userMapper;
    private final String DATETIME_REGEX_FORMAT = "^(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})$";

    private static final String MIN_DATETIME_STRING = "0001-01-01 00:00:00";
    private static final String MAX_DATETIME_STRING = "9999-12-31 23:59:59";


    @PatchMapping("/news/{newsId}/reject")
    @Operation(summary = "Отклонить новость",
            description = "Отклоненная новость должна быть скорректирована автором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость отклонена", content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "403", description = "Новость не подлежит ревью",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public void rejectNews(@PathVariable Long newsId) {
        adminService.rejectNews(newsId);
    }

    @PatchMapping("/news/{newsId}/publish")
    @Operation(summary = "Одобрить новость",
            description = "Одобренная новость доступна для просмотра пользователями")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость одобрена", content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "403", description = "Новость не подлежит ревью",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public void approveNews(@PathVariable Long newsId) {
        adminService.approveNews(newsId);
    }

    @DeleteMapping("/news/{newsId}/delete")
    @Operation(summary = "Удалить новость",
            description = "Удаление новости любого пользователя, запрос может выполнить только администратор")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость удалена", content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "409", description = "Ошибка при попытке удаления чужой новости",
                    content = {@Content(mediaType = "Application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public void deleteNews(@PathVariable Long newsId) {
        adminService.deleteNews(newsId);
    }

    @PostMapping("/users")
    @Operation(summary = "Добавить пользователя",
            description = "Этот запрос может выполнить только администратор, при создании пользователю может быть" +
                    "назначена роль User или Publisher ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован", content = @Content),
            @ApiResponse(responseCode = "400", description = "Необходимо использовать другой username и/или e-mail",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "409", description = "Недостаточно прав для создания администратора",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public UserFullDto addUser(@Parameter(description = "Роль создаваемого пользователя",
            schema = @Schema(allowableValues = {"USER", "PUBLISHER"}))
                               @RequestParam Role role, @Valid @RequestBody NewUserRequest newUserRequest) {
        return userMapper.toUserFullDto(adminService.addUser(newUserRequest, role));
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Удалить пользователя",
            description = "Этот запрос может выполнить только администратор")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь удален", content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "409", description = "Ошибка при попытке удаления администратора",
                    content = {@Content(mediaType = "Application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public void deleteUsers(@PathVariable Long userId) {
        adminService.deleteUsers(userId);
    }

    @PatchMapping("/user/{userId}/ban")
    @Operation(summary = "Наложить бан",
            description = "Этот запрос позволяет наложить или снять бан с пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь забанен/разбанен", content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public void banUser(@PathVariable Long userId,
                        @RequestParam UserStatus userStatus) {
        adminService.banUser(userId, userStatus);
    }

    @GetMapping("/stats")
    @Operation(summary = "Запрос статистики",
            description = "Запрос предоставляет статистику по отслеживаемым эндпоинтам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика предоставлена", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserActivityView.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public List<UserActivityView> getStats(
            @Parameter(description = "Включить в статистику запросы после указанной даты",
                    schema = @Schema(type = "string", pattern = DATETIME_REGEX_FORMAT))
            @RequestParam(defaultValue = MIN_DATETIME_STRING)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @Parameter(description = "Включить в статистику запросы до указанной даты",
                    schema = @Schema(type = "string", pattern = DATETIME_REGEX_FORMAT))
            @RequestParam(defaultValue = MAX_DATETIME_STRING)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam List<Long> userIdList) {
        return adminService.getStats(userIdList, start, end);
    }

}

