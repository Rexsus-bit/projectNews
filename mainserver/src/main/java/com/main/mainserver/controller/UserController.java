package com.main.mainserver.controller;


import com.main.mainserver.exception.controllersExceptions.handler.ApiError;
import com.main.mainserver.mapper.CommentMapper;
import com.main.mainserver.mapper.NewsMapper;
import com.main.mainserver.model.comment.CommentDto;
import com.main.mainserver.model.news.NewsShortDto;
import com.main.mainserver.model.newsApiDto.NewsReportDto;
import com.main.mainserver.security.SecurityUser;
import com.main.mainserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
@Tag(name = "User Controller", description = "Доступен всем пользователям")
public class UserController {

    private final UserService userService;
    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;
    private final String DATE_TIME_REGEX_FORMAT = "^(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})$";

    @GetMapping("/news")
    @Operation(summary = "Поиск новостей",
            description = "Этот запрос возвращает подборку новостей согласно заданным параметрам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получена выборка новостей",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NewsShortDto.class)))),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public List<NewsShortDto> findNews(
            @Parameter(description = "Ключевое слово для поиска")
            @RequestParam(required = false) String text,
            @Parameter(description = "Id авторов новостей для поиска")
            @RequestParam(required = false) List<Long> usersIdList,
            @Parameter(description = "Поиск новостей после указанной даты",
                    schema = @Schema(type = "string", pattern = DATE_TIME_REGEX_FORMAT))
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @Parameter(description = "Поиск новостей до указанной даты",
                    schema = @Schema(type = "string", pattern = DATE_TIME_REGEX_FORMAT))
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @Parameter(description = "Выбрать порядковый номер первого результата из поисковой выдачи")
            @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Выбрать порядкой номер последнего результата из поисковой выдачи")
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal SecurityUser securityUser,
            HttpServletRequest request) {
        return newsMapper.toNewsShortDtoList(userService.findNews(text, usersIdList, rangeStart, rangeEnd, from, size,
                securityUser, request));
    }

    @GetMapping("/news/external")
    @Operation(summary = "Поиск новостей во внешнем источнике",
            description = "Этот запрос возвращает подборку новостей полученную из внешнего источника. " +
                    "Бесплатная версия предоставляет доступ к данным только за последний месяц")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получена выборка новостей",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NewsReportDto.class)))),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "403", description = "Превышение лимита запросов",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "Внутреняя ошибка сервера",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "502", description = "Проблема доступа к внешним сервисам",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public NewsReportDto getNewsFromWeatherApiService(
            @RequestParam String query,
            @Parameter(description = "Поиск новостей после указанной даты", schema = @Schema(type = "string",
                            pattern = DATE_TIME_REGEX_FORMAT))
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @Parameter(description = "Поиск новостей до указанной даты", schema = @Schema(type = "string",
                            pattern = DATE_TIME_REGEX_FORMAT))
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
            @AuthenticationPrincipal SecurityUser securityUser,
            HttpServletRequest request) {
        return userService.getNewsFromWeatherApiService(query, from, to, securityUser, request);
    }

    @PostMapping("/news/comment")
    @Operation(summary = "Добавить комментарий",
            description = "Этот запрос добавляет комментарий к опубликованной новости")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий создан",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Новость для комментария не найдена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public CommentDto addCommentToNews(@RequestParam Long newsId,
                                       @RequestParam String commentText,
                                       @AuthenticationPrincipal SecurityUser securityUser) {
        return commentMapper.toCommentDto(userService.addCommentToNews(newsId, commentText, securityUser));
    }

    @DeleteMapping("/news/comment/delete")
    @Operation(summary = "Удалить комментарий",
            description = "Этот запрос удаляет ранее добавленный комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий удален"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Новость для комментария не найдена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public void deleteCommentToNews(@RequestParam Long commentId,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteCommentToNews(commentId, securityUser);
    }

    @PutMapping("/{newsId}/like")
    @Operation(summary = "Добавить лайк",
            description = "Этот запрос добавляет лайк к опубликованной новости")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий создан",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Новость для комментария не найдена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public void addLikeFromUser(@PathVariable Long newsId, @AuthenticationPrincipal SecurityUser securityUser) {
        userService.addLikeFromUser(newsId, securityUser);
    }

    @DeleteMapping("/{newsId}/like")
    @Operation(summary = "Удалить лайк",
            description = "Этот запрос удаляет ранее добавленный лайк")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Лайк удален"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Новость для лайка не найдена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public void deleteLikeFromUser(@PathVariable Long newsId,
                                   @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteLikeFromUser(newsId, securityUser);
    }

    @GetMapping("/popular")
    @Operation(summary = "Получить наиболее популярные новости",
            description = "Выборка новостей осуществляется по количеству лайков и комментариев")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получена выборка наиболее популярных новостей",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NewsShortDto.class)))),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public List<NewsShortDto> getTopNews(@Parameter(description = "Колличество запрашиваемых новостей")
                                         @RequestParam(defaultValue = "10") Integer limit,
                                         @AuthenticationPrincipal SecurityUser securityUser,
                                         HttpServletRequest request) {
        return newsMapper.toNewsShortDtoList(userService.getTopNews(limit, securityUser, request));
    }

}
