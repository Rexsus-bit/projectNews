package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class UserIsNotFoundException extends NewsAppException {

    public UserIsNotFoundException(String email) {
        super(String.format("Пользователь с email=%s не найден.", email), 114, HttpStatus.NOT_FOUND);
    }

    public UserIsNotFoundException(Long id) {
        super(String.format("Пользователь с id=%d не найден.", id), 114, HttpStatus.NOT_FOUND);
    }

}
