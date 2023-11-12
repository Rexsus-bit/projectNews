package com.main.mainserver.exception.clientNewsApiExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class InternalServerException extends NewsAppException {

    public InternalServerException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Мы уже работаем над устранением проблемы.", errorCode), errorCode,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
