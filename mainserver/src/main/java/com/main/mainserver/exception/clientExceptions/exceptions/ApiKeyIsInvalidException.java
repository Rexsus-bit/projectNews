package com.main.mainserver.exception.clientExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class ApiKeyIsInvalidException extends NewsAppException {

    public ApiKeyIsInvalidException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Загрузка новостей из внешних источников временно недоступна.",
                errorCode), errorCode, HttpStatus.BAD_GATEWAY);
    }
}
