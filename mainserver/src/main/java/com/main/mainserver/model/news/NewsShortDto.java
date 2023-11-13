package com.main.mainserver.model.news;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.main.mainserver.model.comment.CommentDto;
import com.main.mainserver.model.user.UserShortDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
@Builder
public class NewsShortDto {

    @NotBlank
    Long id;
    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotBlank
    String text;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;
    UserShortDto publisher;
    Set<UserShortDto> likesSet;
    List<CommentDto> commentsList;

}
