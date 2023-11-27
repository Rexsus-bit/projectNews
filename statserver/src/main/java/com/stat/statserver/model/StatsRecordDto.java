package com.stat.statserver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsRecordDto {

    private Long userId;
    private String uri;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  LocalDateTime dateTime;

}
