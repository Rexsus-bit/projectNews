package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class NewsIsNotAvaliableException extends NewsAppException {

    public NewsIsNotAvaliableException(Long id) {
        super(String.format("Новость с id=%d не найдена", id), 111, HttpStatus.NOT_FOUND);
    }

}
