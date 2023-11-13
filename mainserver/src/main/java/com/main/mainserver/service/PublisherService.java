package com.main.mainserver.service;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;

public interface PublisherService {
    @Transactional
    News createNews(News news, @AuthenticationPrincipal SecurityUser securityUser, HttpServletRequest request);

    @Transactional
    News updateNews(Long newsId, NewsRequestDto newsRequestDto,
                    @AuthenticationPrincipal SecurityUser securityUser);

    @Transactional
    void deleteNews(Long newsId, @AuthenticationPrincipal SecurityUser securityUser);
}
