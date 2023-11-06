package com.main.mainserver.model.news;


import com.main.mainserver.model.comment.CommentDto;
import com.main.mainserver.model.user.UserFullDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class NewsShortDto {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String text;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dateTime;
    private UserFullDto publisher;
    private Set<UserFullDto> likesList;
    private List<CommentDto> comments;

}
