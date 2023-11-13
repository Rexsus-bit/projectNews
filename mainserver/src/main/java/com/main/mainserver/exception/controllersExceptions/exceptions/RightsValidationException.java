package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;


public class RightsValidationException extends NewsAppException {

    public RightsValidationException(String message) {
        super(message, 112, HttpStatus.CONFLICT);
    }
}
