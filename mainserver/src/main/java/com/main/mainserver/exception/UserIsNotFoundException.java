package com.main.mainserver.exception;

public class UserIsNotFoundException extends RuntimeException {

    public UserIsNotFoundException(String email) {
        super(String.format("User с email=%s не найден.", email));
    }

}
