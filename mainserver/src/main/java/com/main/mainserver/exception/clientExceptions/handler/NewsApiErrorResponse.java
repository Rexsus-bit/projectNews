package com.main.mainserver.exception.clientExceptions.handler;

import lombok.Data;

@Data
public class NewsApiErrorResponse {

    String status;
    String code;
    String message;

}

