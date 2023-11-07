package com.main.mainserver.exception.controllersExceptions.exceptions;

public class InconsistentDataException extends RuntimeException{

    public InconsistentDataException(Long id, String username) {
        super(String.format("Предоставленный id=%d не принадлежит пользователю %s.", id, username));
    }

}
