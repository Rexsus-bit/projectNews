package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class LikeIsExistedException extends NewsAppException {

    public LikeIsExistedException(Long userId, Long newsId) {
        super(String.format("Пользователь с id=%d уже поставил лайк новости с id=%d", userId, newsId), 117,
                HttpStatus.BAD_REQUEST);
    }
}
