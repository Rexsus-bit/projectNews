package com.stat.statserver.service;

import com.stat.statserver.model.StatsRecord;
import com.stat.statserver.model.UserActivityView;
import com.stat.statserver.repository.StatsRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsStatisticsService {

    private final StatsRecordJpaRepository statsRecordJpaRepository;

    public List<UserActivityView> getStats(List<Long> userIdList, LocalDateTime start, LocalDateTime end) {
        return statsRecordJpaRepository.getStats(userIdList, start, end);
    }

    public void saveStatsRecord(StatsRecord statsRecord) {
        statsRecordJpaRepository.save(statsRecord);
    }
}
