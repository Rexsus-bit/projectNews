package com.main.mainserver.exception.clientNewsApiExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class ApiKeyIsNotProvidedException extends NewsAppException {

    public ApiKeyIsNotProvidedException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Мы уже работаем над устранением проблемы.", errorCode), errorCode,
                HttpStatus.BAD_REQUEST);
    }

}


