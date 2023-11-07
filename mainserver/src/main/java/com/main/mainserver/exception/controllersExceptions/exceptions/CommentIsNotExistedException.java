package com.main.mainserver.exception.controllersExceptions.exceptions;

import com.main.mainserver.exception.NewsAppException;
import org.springframework.http.HttpStatus;

public class CommentIsNotExistedException extends NewsAppException {

    public CommentIsNotExistedException(Long commentId, Long userId) {
        super(String.format("Комментарий с id=%d у пользователя с id=%d не найден.", commentId, userId), 116,
                HttpStatus.BAD_REQUEST);
    }

}
