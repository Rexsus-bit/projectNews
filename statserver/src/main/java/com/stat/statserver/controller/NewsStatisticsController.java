package com.stat.statserver.controller;


import com.stat.statserver.model.UserActivityView;
import com.stat.statserver.service.NewsStatisticsService;
import com.stat.statserver.mapper.StatsRecordMapper;
import com.stat.statserver.model.StatsRecordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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


    @PostMapping ("/record")
    public void saveStatsRecord(@RequestBody StatsRecordDto statsRecordDto) {
        newsStatisticsService.saveStatsRecord(statsRecordMapper.toStatsRecord(statsRecordDto));
    }

    @GetMapping("/info")
    public List<UserActivityView>  getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                            @RequestParam List<Long> userIdList) {
        return newsStatisticsService.getStats(userIdList, start, end);
    }

}
