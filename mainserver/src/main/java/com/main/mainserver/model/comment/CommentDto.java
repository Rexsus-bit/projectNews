package com.main.mainserver.model.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.main.mainserver.model.user.UserShortDto;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CommentDto {
    Long id;
    String text;
    UserShortDto owner;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationTime;
}
