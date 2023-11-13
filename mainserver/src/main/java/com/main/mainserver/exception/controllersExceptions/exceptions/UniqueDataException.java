package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class UniqueDataException extends NewsAppException {

    public UniqueDataException(String message) {
        super(message, 112, HttpStatus.BAD_REQUEST);
    }

}
