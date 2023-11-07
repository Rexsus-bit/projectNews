package com.main.mainserver.model.comment;

import com.main.mainserver.model.user.UserShortDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CommentDto {
    String text;
    UserShortDto owner;
    LocalDateTime creationTime;
}
