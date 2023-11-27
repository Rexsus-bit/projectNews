package com.main.mainserver.model.newsApiDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsReportDto {

    String status;
    Integer totalResults;
    List<NewsApiDto> articles;

}



