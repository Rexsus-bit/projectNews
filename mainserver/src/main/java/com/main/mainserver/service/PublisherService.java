package com.main.mainserver.service;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;

public interface PublisherService {

    News createNews(News news, SecurityUser securityUser, HttpServletRequest request);

    News updateNews(Long newsId, NewsRequestDto newsRequestDto, SecurityUser securityUser);

    void deleteNews(Long newsId, SecurityUser securityUser);

}
