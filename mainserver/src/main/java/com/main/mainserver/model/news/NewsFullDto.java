package com.main.mainserver.model.news;

import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class NewsFullDto {

    @NotBlank
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String text;
    NewsStatus newsStatus;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateTime;
    private User publisher;
    private Set<User> likesList;
    private List<Comment> comments;



}
