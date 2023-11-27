package com.main.mainserver.service;

import com.main.mainserver.clientStats.StatisticClient;
import com.main.mainserver.exception.controllersExceptions.exceptions.NewsIsNotAvaliableException;
import com.main.mainserver.exception.controllersExceptions.exceptions.UserIsNotFoundException;
import com.main.mainserver.exception.controllersExceptions.exceptions.RightsValidationException;
import com.main.mainserver.mapper.NewsMapper;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.model.user.User;
import com.main.mainserver.repository.NewsJpaRepository;
import com.main.mainserver.repository.UserJPARepository;
import com.main.mainserver.security.SecurityUser;
import com.stat.statserver.model.StatsRecordDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final NewsJpaRepository newsJpaRepository;
    private final UserJPARepository userJPARepository;
    private final NewsMapper newsMapper;
    private final StatisticClient statisticClient;

    @Override
    @Transactional
    public News createNews(News news, SecurityUser securityUser, HttpServletRequest request) {
        statisticClient.sendStats(new StatsRecordDto(securityUser.getId(), request.getRequestURI(), LocalDateTime.now()));
        User publisher = userJPARepository.findById(securityUser.getId())
                .orElseThrow(() -> new UserIsNotFoundException(securityUser.getId()));
        news.setPublisher(publisher);
        return newsJpaRepository.save(news);
    }

    @Override
    @Transactional
    public News updateNews(Long newsId, NewsRequestDto newsRequestDto, SecurityUser securityUser) {
        News news = newsJpaRepository.findById(newsId).orElseThrow(() -> new NewsIsNotAvaliableException(newsId));
        if (!securityUser.getId().equals(news.getPublisher().getId())) {
            throw new RightsValidationException("Вы не можете редактировать новость другого автора");
        }
        newsMapper.updateNews(newsRequestDto, news);
        return newsJpaRepository.save(news);
    }

    @Override
    @Transactional
    public void deleteNews(Long newsId, SecurityUser securityUser) {
        News news = newsJpaRepository.findById(newsId).orElseThrow(() -> new NewsIsNotAvaliableException(newsId));
        if (!securityUser.getId().equals(news.getPublisher().getId())) {
            throw new RightsValidationException("Вы не можете удалить новость другого автора");
        }
        newsJpaRepository.deleteById(newsId);
    }

}
