package com.main.mainserver.exception.clientResponseExceptions.exceptions;


import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class ParameterIsInvalidException extends NewsAppException {

    public ParameterIsInvalidException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Параметры запроса были введены неверно.", errorCode), errorCode,
                HttpStatus.BAD_REQUEST);
    }

}
