package com.main.mainserver.exception.controllersExceptions.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder
@Schema(description = "В ApiError вовзращается информация о возникшей ошибке")
public class ApiError {

    @Schema(description = "Внутренний код ошибки")
    Integer errorCode;
    @Schema(description = "Сообщение(-я) о произошедшей ошибке(-ах)")
    Map<String, String> messages;
    @Schema(description = "HttpStatus")
    HttpStatus status;
    @Schema(description = "Время ошибки")
    LocalDateTime timestamp;

}
