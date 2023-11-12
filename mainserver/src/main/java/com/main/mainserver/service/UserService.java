package com.main.mainserver.service;

import com.main.mainserver.clientNewsApi.NewsApiRestClient;
import com.main.mainserver.clientStats.StatisticClient;
import com.main.mainserver.exception.controllersExceptions.exceptions.CommentIsNotExistedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.LikeIsExistedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.LikeIsNotExistedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.NewsIsNotExistedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.NewsIsNotPublishedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.UserIsNotFoundException;
import com.main.mainserver.model.Like;
import com.main.mainserver.model.comment.Comment;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsStatus;
import com.main.mainserver.model.newsApiDto.NewsReportDto;
import com.main.mainserver.model.user.User;
import com.main.mainserver.repository.CommentJPARepository;
import com.main.mainserver.repository.LikeJpaRepository;
import com.main.mainserver.repository.NewsCriteriaRepository;
import com.main.mainserver.repository.NewsJpaRepository;
import com.main.mainserver.repository.UserJPARepository;
import com.main.mainserver.security.SecurityUser;
import com.stat.statserver.model.StatsRecordDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJPARepository userJPARepository;
    private final NewsJpaRepository newsJpaRepository;
    private final CommentJPARepository commentJPARepository;
    private final LikeJpaRepository likeJpaRepository;
    private final NewsApiRestClient newsApiRestClient;
    private final NewsCriteriaRepository newsCriteriaRepository;
    private final StatisticClient statisticClient;

    // TODO взаимодействие только с PUBLISHED
    // TODO подумать где нужно @Transactional

    @Transactional
    public List<News> findNews(String text, List<Long> usersIdList, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                               Integer from, Integer size, SecurityUser securityUser, HttpServletRequest request) {
        statisticClient.sendStatisticsInfo(new StatsRecordDto(securityUser.getId(), request.getRequestURI(),
                LocalDateTime.now()));
        return newsCriteriaRepository.findNewsByCustomCriteria(text, usersIdList, rangeStart, rangeEnd, from, size);
    } // checked

    public NewsReportDto getNewsFromWeatherApiService(String query, LocalDate from, LocalDate to,
                                                      SecurityUser securityUser, HttpServletRequest request) {
        statisticClient.sendStatisticsInfo(new StatsRecordDto(securityUser.getId(), request.getRequestURI(),
                LocalDateTime.now()));
        return newsApiRestClient.requestNews(query, from, to);
    } // checked

    @Transactional
    public Comment addCommentToNews(Long newsId, String commentText, SecurityUser securityUser) {
        News news = newsJpaRepository.findById(newsId)
                .orElseThrow(() -> new NewsIsNotExistedException(newsId));
        if (!news.getNewsStatus().equals(NewsStatus.PUBLISHED)) {
            throw new NewsIsNotPublishedException(newsId);
        }
        User user = userJPARepository.findById(securityUser.getId()).orElseThrow(()
                -> new UserIsNotFoundException(securityUser.getId()));
        Comment comment = new Comment(null, commentText, news, user, LocalDateTime.now());
        return commentJPARepository.save(comment);
    } // checked

    @Transactional
    public void deleteCommentToNews(Long commentId, SecurityUser securityUser) {
        int cnt = commentJPARepository.deleteCommentByIdAndOwnerId(commentId, securityUser.getId());
        if (cnt == 0) {
            throw new CommentIsNotExistedException(commentId, securityUser.getId());
        }
    } // checked

    @Transactional
    public void addLikeFromUser(Long newsId, SecurityUser securityUser) {
        if (!newsJpaRepository.existsById(newsId)) throw new NewsIsNotExistedException(newsId);
        try {
            likeJpaRepository.save(new Like(null, newsId, securityUser.getId()));
        } catch (DataIntegrityViolationException e) {
            throw new LikeIsExistedException(securityUser.getId(), newsId);
        }
    } // checked

    @Transactional
    public void deleteLikeFromUser(Long newsId, SecurityUser securityUser) {
        int count = likeJpaRepository.deleteLikeByUserIdAndNewsId(securityUser.getId(), newsId);
        if (count == 0) {
            throw new LikeIsNotExistedException(newsId, securityUser.getId());
        } // checked
    }

    @Transactional // сначала по лайкам потом по комментам
    public List<News> getTopNews(Integer limit, SecurityUser securityUser, HttpServletRequest request) {
        statisticClient.sendStatisticsInfo(new StatsRecordDto(securityUser.getId(), request.getRequestURI(),
                LocalDateTime.now()));
        return newsJpaRepository.findAllAndSortByLikesAndComments(limit);
    } // checked

}
