package com.stat.statserver.mapper;

import com.stat.statserver.model.StatsRecord;
import com.stat.statserver.model.StatsRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatsRecordMapper {

    StatsRecord toStatsRecord(StatsRecordDto statsRecordDto);


}
