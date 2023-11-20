package com.main.mainserver.service;

import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import com.stat.statserver.model.UserActivityView;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {

    void rejectNews(Long newsId);

    void approveNews(Long newsId);

    void deleteNews(Long newsId);

    User addUser(NewUserRequest newUserRequest, Role role);

    void deleteUsers(Long userId);

    void banUser(Long userId, UserStatus userStatus);

    List<UserActivityView> getStats(List<Long> userIdList, LocalDateTime start, LocalDateTime end);
}
