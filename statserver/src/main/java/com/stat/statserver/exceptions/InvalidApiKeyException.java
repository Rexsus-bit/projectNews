package com.stat.statserver.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidApiKeyException extends StatsAppException {

    public InvalidApiKeyException() {
        super("Invalid API Key", 401, HttpStatus.UNAUTHORIZED);
    }
}
