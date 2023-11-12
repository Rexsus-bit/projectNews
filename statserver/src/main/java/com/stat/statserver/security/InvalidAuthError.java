package com.stat.statserver.security;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
public class InvalidAuthError {
    Integer errorCode;
    String message;
    HttpStatus status;
}
