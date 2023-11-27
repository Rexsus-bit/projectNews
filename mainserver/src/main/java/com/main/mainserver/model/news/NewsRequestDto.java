package com.main.mainserver.model.news;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class NewsRequestDto {

    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotBlank
    String text;

}
