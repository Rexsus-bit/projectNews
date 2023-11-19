package com.main.mainserver.exception.clientNewsApiExceptions.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.InternalServerException;
import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.RateIsLimitedException;
import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.ApiKeyIsNotProvidedException;
import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.ApiKeyIsInvalidException;
import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.ApiKeyIsDisabledException;
import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.ApiKeyIsExhaustedException;
import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.ParameterIsInvalidException;
import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.ParameterIsNotIndicatedException;
import com.main.mainserver.exception.clientNewsApiExceptions.exceptions.UnknownServerInternalException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;


@Component
public class NewsClientResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {
        return (
                httpResponse.getStatusCode().is4xxClientError()
                        || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        NewsApiErrorResponse newsApiErrorResponse = objectMapper
                .readValue(httpResponse.getBody(), NewsApiErrorResponse.class);
        String errorCode = newsApiErrorResponse.getCode();

            switch (errorCode) {
                case ("apiKeyDisabled") -> throw new ApiKeyIsDisabledException(100);
                case ("apiKeyInvalid") -> throw new ApiKeyIsInvalidException(101);
                case ("apiKeyExhausted") -> throw new ApiKeyIsExhaustedException(102);
                case ("apiKeyMissing") -> throw new ApiKeyIsNotProvidedException(103);
                case ("parameterInvalid") -> throw new ParameterIsInvalidException(104);
                case ("parametersMissing") -> throw new ParameterIsNotIndicatedException(105);
                case ("rateLimited") -> throw new RateIsLimitedException(106);
                case ("unexpectedError") -> throw new InternalServerException(107);
                default -> throw new UnknownServerInternalException(108);
            }

    }
}
