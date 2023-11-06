package com.main.mainserver.service;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    public News rejectNews(Long newsId) {
    return null;
    }

    public News approveNews(Long newsId) {
    return null;
    }


    public void deleteNews(Long newsId) {
    }

    public User addUser(NewUserRequest newUserRequest) {
        return null;
    }

    public void deleteUsers(Long userId) {
    }

    public User addPublisher(NewUserRequest newUserRequest) {
        return null;
    }

    public void deletePublisher(Long userId) {
    }
}
