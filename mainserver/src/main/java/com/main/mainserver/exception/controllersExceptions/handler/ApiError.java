package com.main.mainserver.exception.controllersExceptions.handler;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Value
@Builder
public class ApiError {
    Integer errorCode;
    String message;
    HttpStatus status;
    LocalDateTime timestamp;
}
