package com.main.mainserver.controller;


import com.main.mainserver.exception.controllersExceptions.handler.ApiError;
import com.main.mainserver.mapper.NewsMapper;
import com.main.mainserver.model.news.NewsShortDto;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.security.SecurityUser;
import com.main.mainserver.service.PublisherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j // TODO
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/publish")
@Tag(name = "Publisher Controller", description = "Доступен администраторам и авторам новостей")
public class PublisherController {

    private final PublisherService publisherService;

    private final NewsMapper newsMapper;

    @PostMapping("/news")
    @Operation(summary = "Создать новость",
            description = "Этот запрос может выполнить только авторизованный пользователь с ролью publisher или admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость создана",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsShortDto.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public NewsShortDto createNews(@Valid @RequestBody NewsRequestDto newsRequestDto,
                                   @AuthenticationPrincipal SecurityUser securityUser,
                                   HttpServletRequest request) {
        return newsMapper.toNewsShortDto(publisherService
                .createNews(newsMapper.toNews(newsRequestDto), securityUser, request));
    }

    @PatchMapping("/news/{newsId}")
    @Operation(summary = "Обновить новость",
            description = "Этот запрос может выполнить только авторизованный пользователь с ролью publisher или " +
                    "admin, который создал новость")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость обновлена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsShortDto.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "409", description = "Ошибка при попытке редактирования чужой новости",
                    content = {@Content(mediaType = "Application/json",
                            schema = @Schema(implementation = ApiError.class))})})
    public NewsShortDto updateNews(@PathVariable Long newsId,
                                   @RequestBody @Valid NewsRequestDto newsRequestDto,
                                   @AuthenticationPrincipal SecurityUser securityUser) {
        return newsMapper.toNewsShortDto(publisherService.updateNews(newsId, newsRequestDto, securityUser));
    }

    @DeleteMapping("/news/delete/{newsId}")
    @Operation(summary = "Удалить новость",
            description = "Удалить новость может только авторизованный пользователь с ролью publisher или " +
                    "admin, который её создал")
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
    public void deleteNews(@PathVariable Long newsId, @AuthenticationPrincipal SecurityUser securityUser) {
        publisherService.deleteNews(newsId, securityUser);
    }

}
