package com.main.mainserver.exception.clientNewsApiExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class ApiKeyIsInvalidException extends NewsAppException {

    public ApiKeyIsInvalidException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Внешний сервис временно недоступен.",
                errorCode), errorCode, HttpStatus.BAD_GATEWAY);
    }
}
