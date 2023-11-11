package com.stat.statserver;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/stat")
public class NewsStatisticsController {

    NewsStatisticsService newsStatisticsService;

    @PostMapping ("/hit/")
    public String saveStatsRecord() {
//        return newsStatisticsService.saveStatsRecord;
        return null;
    }

    @GetMapping("/info/")
    public String getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                           @RequestParam List<String> uris,
                           @RequestParam(defaultValue = "false") Boolean unique) {
        return newsStatisticsService.getStats(start, end, uris, unique);
    }

}
