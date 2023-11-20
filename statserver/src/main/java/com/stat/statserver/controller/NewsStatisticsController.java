package com.stat.statserver.controller;


import com.stat.statserver.model.UserActivityView;
import com.stat.statserver.security.StatsErrorApi;
import com.stat.statserver.service.NewsStatisticsService;
import com.stat.statserver.mapper.StatsRecordMapper;
import com.stat.statserver.model.StatsRecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final StatsRecordMapper statsRecordMapper;


    @PostMapping("/record")
    @Operation(summary = "Сохранение статистики",
            description = "Запрос сохраняет статистику об использовании пользователем того или иного эндпоинта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика сохранена"),
            @ApiResponse(responseCode = "401", description = "Ошибка авторизации",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatsErrorApi.class))})})
    public void saveStatsRecord(@RequestBody StatsRecordDto statsRecordDto) {
        newsStatisticsService.saveStatsRecord(statsRecordMapper.toStatsRecord(statsRecordDto));
    }

    @GetMapping("/info")
    @Operation(summary = "Запрос статистики",
            description = "Запрос предоставляет статистику по отслеживаемым эндпоинтам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика предоставлена", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = UserActivityView.class))}),
            @ApiResponse(responseCode = "401", description = "Ошибка авторизации",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatsErrorApi.class))})})
    public List<UserActivityView> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam List<Long> userIdList) {
        return newsStatisticsService.getStats(userIdList, start, end);
    }

}
