package com.main.mainserver.model.newsApiDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiDto {

    @JsonProperty("source")
    SourceDto sourceDto;
    String author;
    String title;
    String description;
    String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedAt;
    String content;

}
