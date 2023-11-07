package com.main.mainserver.model.news;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NewsRequestDto {

    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotBlank
    String text;

}
