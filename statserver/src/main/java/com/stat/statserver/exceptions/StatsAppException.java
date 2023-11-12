package com.stat.statserver.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class StatsAppException extends RuntimeException{

    private final Integer errorCode;
    private final HttpStatus httpStatus;

    public StatsAppException(String message, Integer errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
