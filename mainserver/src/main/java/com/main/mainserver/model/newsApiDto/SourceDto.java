package com.main.mainserver.model.newsApiDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class SourceDto {

    private final String name;

    @JsonCreator
    public SourceDto(String name) {
        this.name = name;
    }

}
