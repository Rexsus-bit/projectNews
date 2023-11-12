package com.main.mainserver.exception.clientNewsApiExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class ParameterIsNotIndicatedException extends NewsAppException {

    public ParameterIsNotIndicatedException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Для корректной работы необходимо указать параметры запроса.",
                errorCode), errorCode, HttpStatus.BAD_REQUEST);
    }

}
