package com.main.mainserver.model.comment;

import com.main.mainserver.model.user.UserFullDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class CommentDto {
    private String message;
    private UserFullDto owner;
    private LocalDateTime creationTime;
}
