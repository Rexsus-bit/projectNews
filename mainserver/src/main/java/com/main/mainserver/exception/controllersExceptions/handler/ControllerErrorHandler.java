package com.main.mainserver.exception.controllersExceptions.handler;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = {"com.main.mainserver.controller"})
public class ControllerErrorHandler {

    private final String MESSAGE = "message";

    @ExceptionHandler(NewsAppException.class)
    public ResponseEntity<ApiError> handleNotFound(final NewsAppException e) {
        return new ResponseEntity<>(new ApiError(e.getErrorCode(),
                Map.of(MESSAGE, e.getMessage()),
                e.getHttpStatus(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        Map<String, String> messages = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            messages.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new ApiError(177,
                messages,
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException() {
        return new ResponseEntity<>(new ApiError(357,
                Map.of(MESSAGE, "Введены некорректные данные"),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleException() {
        final int errorCode = 9000;
        return new ResponseEntity<>(new ApiError(errorCode,
                Map.of(MESSAGE, String.format("Произошла ошибка № %d. Мы уже работаем над ее устранением.", errorCode)),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
