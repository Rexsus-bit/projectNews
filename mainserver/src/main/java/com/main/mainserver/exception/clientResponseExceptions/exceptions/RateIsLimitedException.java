package com.main.mainserver.exception.clientResponseExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class RateIsLimitedException extends NewsAppException {

    public RateIsLimitedException(int errorCode) {
        super(String.format("Произошла ошибка № %d. Лимит запросов на поиск на внешних ресурсах исчерпан, попробуйте " +
                "позже или обратитесь к администратору сервиса.", errorCode), errorCode, HttpStatus.FORBIDDEN);
    }
}
