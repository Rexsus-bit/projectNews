package com.main.mainserver.service;

import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsShortDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {


    public List<News> findNews(LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        return null;
    }

    public static void getNewsFromWeatherApiService() {
    }
    public Comment addCommentToNews(Long userId, Long newsId, String commentText) {
        return null;
    }

    public void addLikeFromUser(Long newsId, Long userId) {
    }

    public void deleteLikeFromUser(Long newsId, Long userId) {
    }

    public List<NewsShortDto> getTopNews(int count) {
        return null;
    }
}
