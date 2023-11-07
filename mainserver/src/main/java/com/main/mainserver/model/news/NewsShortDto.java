package com.main.mainserver.model.news;


import com.main.mainserver.model.comment.CommentDto;
import com.main.mainserver.model.user.UserShortDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDateTime dateTime;
    UserShortDto publisher;
    Set<UserShortDto> likesSet;
    List<CommentDto> commentsList;

}
