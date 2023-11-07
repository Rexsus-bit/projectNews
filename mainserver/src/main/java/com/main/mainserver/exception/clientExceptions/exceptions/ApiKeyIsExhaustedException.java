package com.main.mainserver.exception.clientExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class ApiKeyIsExhaustedException extends NewsAppException {

    public ApiKeyIsExhaustedException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Превышен лимит запросов в месяц.", errorCode), errorCode, HttpStatus.FORBIDDEN);
    }
}
