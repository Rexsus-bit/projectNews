package com.stat.statserver.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = {"com.stat.statserver.controller"})
public class ControllerErrorHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StatsErrorApi> handleMethodArgumentTypeMismatchException() {
        return new ResponseEntity<>(new StatsErrorApi(977, "Некорректный запрос",
                HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<StatsErrorApi> handleException() {
        final int errorCode = 9001;
        return new ResponseEntity<>(new StatsErrorApi(errorCode,
                String.format("Произошла ошибка № %d. Мы уже работаем над ее устранением.", errorCode),
                HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
