package com.stat.statserver.controller;


import com.stat.statserver.model.UserActivityView;
import com.stat.statserver.handler.StatsErrorApi;
import com.stat.statserver.service.NewsStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/stat")
public class NewsStatisticsController {

    private final NewsStatisticsService newsStatisticsService;

    @GetMapping("/info")
    @Operation(summary = "Запрос статистики",
            description = "Запрос предоставляет статистику по отслеживаемым эндпоинтам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика предоставлена", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserActivityView.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректно введены параметры запроса",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatsErrorApi.class))}),
            @ApiResponse(responseCode = "401", description = "Ошибка авторизации",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatsErrorApi.class))})})
    public List<UserActivityView> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam List<Long> userIdList) {
        return newsStatisticsService.getStats(userIdList, start, end);
    }

}
