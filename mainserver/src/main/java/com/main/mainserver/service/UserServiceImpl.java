package com.main.mainserver.service;

import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.newsApiDto.NewsReportDto;
import com.main.mainserver.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserServiceImpl {
    @Transactional
    List<News> findNews(String text, List<Long> usersIdList, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                        Integer from, Integer size, SecurityUser securityUser, HttpServletRequest request);

    NewsReportDto getNewsFromWeatherApiService(String query, LocalDate from, LocalDate to,
                                               SecurityUser securityUser, HttpServletRequest request);

    @Transactional
    Comment addCommentToNews(Long newsId, String commentText, SecurityUser securityUser);

    @Transactional
    void deleteCommentToNews(Long commentId, SecurityUser securityUser);

    @Transactional
    void addLikeFromUser(Long newsId, SecurityUser securityUser);

    @Transactional
    void deleteLikeFromUser(Long newsId, SecurityUser securityUser);

    @Transactional
        // сначала по лайкам потом по комментам
    List<News> getTopNews(Integer limit, SecurityUser securityUser, HttpServletRequest request);
}
