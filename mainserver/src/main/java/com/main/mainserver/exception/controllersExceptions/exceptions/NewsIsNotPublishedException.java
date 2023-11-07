package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class NewsIsNotPublishedException extends NewsAppException {

    public NewsIsNotPublishedException(Long newsId) {
        super(String.format("Невозможно оставить комментарий к новости с id=%d ", newsId), 115, HttpStatus.BAD_REQUEST);
    }
}
