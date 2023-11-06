package com.main.mainserver.service;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.UpdateNewsRequest;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    public News createNews(News news, Long publisherId) {
        return news;
    }

    public void deleteNews(Long publisherId, Long newsId) {
    }

    public News updateNews(UpdateNewsRequest updateNewsRequest, Long publisherId) {

        return null;
    }
}
