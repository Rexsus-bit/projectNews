package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class LikeIsNotExistedException extends NewsAppException {

    public LikeIsNotExistedException(Long newsId, Long userId) {
        super(String.format("У новости с id=%d отсутствует лайк пользователя с id=%d.", newsId, userId), 118,
                HttpStatus.NOT_FOUND);
    }

}
