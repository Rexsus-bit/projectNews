package com.main.mainserver.exception.clientResponseExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;


public class InvalidRequestTimePeriodException extends NewsAppException {

    public InvalidRequestTimePeriodException(int errorCode, LocalDate restrictionDate, LocalDate current) {
        super(String.format("Произошла ошибка № %d. Новости могут бы запрошены только за последний месяц не не ранее %s." +
                        " Вы пытаетесь запросить новости начиная с %s.", errorCode, restrictionDate, current),
                errorCode, HttpStatus.BAD_REQUEST);
    }
}
