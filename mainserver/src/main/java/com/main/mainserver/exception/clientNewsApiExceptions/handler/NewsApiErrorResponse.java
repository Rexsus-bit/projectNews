package com.main.mainserver.exception.clientNewsApiExceptions.handler;

import lombok.Data;

@Data
public class NewsApiErrorResponse {

    String status;
    String code;
    String message;

}

