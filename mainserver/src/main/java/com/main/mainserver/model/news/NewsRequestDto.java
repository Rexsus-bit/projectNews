package com.main.mainserver.model.news;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewsRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String text;

}
