package com.main.mainserver.exception.clientResponseExceptions.clientResponseHandler.newsApiClientResponseErrorHandler;

import lombok.Data;

@Data
public class NewsApiErrorResponse {

    String status;
    String code;
    String message;

}

