package com.main.mainserver.service;

import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public interface AdminService {

    void rejectNews(Long newsId);

    void approveNews(Long newsId);

    void deleteNews(Long newsId);

    User addUser(NewUserRequest newUserRequest, Role role);

    void deleteUsers(Long userId);

    void banUser(Long userId, UserStatus userStatus);
}
