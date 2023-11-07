package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class NewsForApprovalException extends NewsAppException {

    public NewsForApprovalException(Long newsId) {
        super(String.format("Произошла ошибка № %d. Новость с id = %d не подлежит ревью.", 110, newsId), 110,
                HttpStatus.FORBIDDEN);
    }

}
