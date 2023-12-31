package com.main.mainserver.exception.clientResponseExceptions.clientResponseHandler.statsClientResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.mainserver.exception.clientResponseExceptions.exceptions.ApiKeyIsInvalidException;
import com.main.mainserver.exception.clientResponseExceptions.exceptions.InternalServerException;
import com.main.mainserver.exception.clientResponseExceptions.exceptions.ParameterIsInvalidException;
import com.main.mainserver.exception.clientResponseExceptions.exceptions.UnknownServerInternalException;
import com.stat.statserver.handler.StatsErrorApi;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class StatsClientResponseErrorHandler implements ResponseErrorHandler {

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
        StatsErrorApi ErrorDto = objectMapper
                .readValue(httpResponse.getBody(), StatsErrorApi.class);

        int errorCode = ErrorDto.getErrorCode();

        switch (errorCode) {
            case (1002) -> throw new ApiKeyIsInvalidException(445);
            case (977) -> throw new ParameterIsInvalidException(768);
            case (901) -> throw new InternalServerException(897);
            default -> throw new UnknownServerInternalException(450);
        }

    }

}
