package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ValidationException extends NewsAppException {

    public ValidationException(String message) {
        super(message, 112, HttpStatus.BAD_REQUEST);
    }
}
