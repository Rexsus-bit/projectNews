package com.main.mainserver.service;

import com.main.mainserver.exception.controllersExceptions.exceptions.NewsIsNotExistedException;
import com.main.mainserver.exception.controllersExceptions.exceptions.UserIsNotFoundException;
import com.main.mainserver.exception.controllersExceptions.exceptions.ValidationException;
import com.main.mainserver.mapper.NewsMapper;
import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.model.user.User;
import com.main.mainserver.repository.NewsJpaRepository;
import com.main.mainserver.repository.UserJPARepository;
import com.main.mainserver.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final NewsJpaRepository newsJpaRepository;
    private final UserJPARepository userJPARepository;
    private final NewsMapper newsMapper;

    @Transactional
    public News createNews(News news, @AuthenticationPrincipal  SecurityUser securityUser) {
        User publisher = userJPARepository.findById(securityUser.getId())
                .orElseThrow(() -> new UserIsNotFoundException(securityUser.getId()));
        news.setPublisher(publisher);
        return newsJpaRepository.save(news);
    } // checked

    @Transactional
    public News updateNews(Long newsId, NewsRequestDto newsRequestDto,
                           @AuthenticationPrincipal SecurityUser securityUser) {
        News news = newsJpaRepository.findById(newsId).orElseThrow(() -> new NewsIsNotExistedException(newsId));
        if (!securityUser.getId().equals(news.getPublisher().getId())) {
            throw new ValidationException("Вы не можете редактировать новость другого автора");
        }
        newsMapper.updateNews(newsRequestDto, news);
        return newsJpaRepository.save(news);
    } // checked

    @Transactional
    public void deleteNews(Long newsId, @AuthenticationPrincipal SecurityUser securityUser) {
        News news = newsJpaRepository.findById(newsId).orElseThrow(() -> new NewsIsNotExistedException(newsId));
        if (!securityUser.getId().equals(news.getPublisher().getId())) {
            throw new ValidationException("Вы не можете удалить новость другого автора");
        }
        newsJpaRepository.deleteById(newsId);
    } // checked

}
