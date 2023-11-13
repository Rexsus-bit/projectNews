package com.main.mainserver.service;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface PublisherService {

    News createNews(News news, @AuthenticationPrincipal SecurityUser securityUser, HttpServletRequest request);

    News updateNews(Long newsId, NewsRequestDto newsRequestDto,
                    @AuthenticationPrincipal SecurityUser securityUser);

    void deleteNews(Long newsId, @AuthenticationPrincipal SecurityUser securityUser);
}
