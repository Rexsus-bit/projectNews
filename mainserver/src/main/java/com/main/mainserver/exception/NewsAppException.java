package com.main.mainserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class NewsAppException extends RuntimeException{

    private final Integer errorCode;
    private final HttpStatus httpStatus;

    public NewsAppException(String message, Integer errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
