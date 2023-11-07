package com.main.mainserver.exception.controllersExceptions.handler;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = {"com.main.mainserver.controller"})
public class ControllerErrorHandler {

    @ExceptionHandler({NewsAppException.class})
    public ResponseEntity<ApiError> handleNotFound(final NewsAppException e) {
        return new ResponseEntity<>(new ApiError(e.getErrorCode(),
                e.getMessage(),
                e.getHttpStatus(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)), e.getHttpStatus());
    }
    // TODO обрезать время через формат?

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(Throwable.class)
        public ResponseEntity<ApiError> handleException(Throwable e){
        final int errorCode = 9000;
        return new ResponseEntity<>(new ApiError(errorCode,
                    String.format("Произошла ошибка № %d. Мы уже работаем над ее устранением.", errorCode),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)), HttpStatus.INTERNAL_SERVER_ERROR);
        }


}
