package com.main.mainserver.model.news;

import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.user.User;
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
public class NewsFullDto {

    @NotBlank
    Long id;
    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotBlank
    String text;
    NewsStatus newsStatus;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDateTime dateTime;
    User publisher;
    Set<User> likesList;
    List<Comment> comments;

}
