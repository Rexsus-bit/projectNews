package com.stat.statserver.security;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
public class ErrorDto {
    Integer errorCode;
    String message;
    HttpStatus status;
}
