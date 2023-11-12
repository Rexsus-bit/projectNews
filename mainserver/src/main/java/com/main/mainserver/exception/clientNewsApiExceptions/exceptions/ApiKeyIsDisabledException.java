package com.main.mainserver.exception.clientNewsApiExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class ApiKeyIsDisabledException extends NewsAppException {

    public ApiKeyIsDisabledException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Мы уже работаем над ее устранением.", errorCode), errorCode,
                HttpStatus.BAD_GATEWAY);
    }


}
