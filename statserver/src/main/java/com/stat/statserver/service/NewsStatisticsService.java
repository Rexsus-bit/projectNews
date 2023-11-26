package com.stat.statserver.service;

import com.stat.statserver.mapper.StatsRecordMapper;
import com.stat.statserver.model.StatsRecord;
import com.stat.statserver.model.StatsRecordDto;
import com.stat.statserver.model.UserActivityView;
import com.stat.statserver.repository.StatsRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsStatisticsService {

    private final StatsRecordJpaRepository statsRecordJpaRepository;
    private final StatsRecordMapper statsRecordMapper;
    public static final String TOPIC = "statsTopic";

    public List<UserActivityView> getStats(List<Long> userIdList, LocalDateTime start, LocalDateTime end) {
        return statsRecordJpaRepository.getStats(userIdList, start, end);
    }

    @KafkaListener(topics = TOPIC, groupId = "statsGroup")
    public void saveStatsRecord(StatsRecordDto statsRecordDto) {
        StatsRecord statsRecord = statsRecordMapper.toStatsRecord(statsRecordDto);
        statsRecordJpaRepository.save(statsRecord);
    }

}
