package com.main.mainserver.service;

import com.main.mainserver.exception.controllersExceptions.exceptions.NewsForApprovalException;
import com.main.mainserver.exception.controllersExceptions.exceptions.NewsIsNotAvaliableException;
import com.main.mainserver.exception.controllersExceptions.exceptions.UniqueDataException;
import com.main.mainserver.exception.controllersExceptions.exceptions.UserIsNotFoundException;
import com.main.mainserver.exception.controllersExceptions.exceptions.RightsValidationException;
import com.main.mainserver.mapper.UserMapper;
import com.main.mainserver.model.news.NewsStatus;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.repository.NewsJpaRepository;
import com.main.mainserver.repository.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserJPARepository userJPARepository;
    private final NewsJpaRepository newsJpaRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void rejectNews(Long newsId) {
        int cnt = newsJpaRepository.setNewsStatus(NewsStatus.REJECTED, newsId);
        if (cnt == 0) {
            throw new NewsForApprovalException(newsId);
        }
    }

    @Override
    @Transactional
    public void approveNews(Long newsId) {
        int cnt = newsJpaRepository.setNewsStatus(NewsStatus.PUBLISHED, newsId);
        if (cnt == 0) {
            throw new NewsForApprovalException(newsId);
        }
    }

    @Override
    @Transactional
    public void deleteNews(Long newsId) {
        int cnt = newsJpaRepository.deleteNewsById(newsId);
        if (cnt == 0) throw new NewsIsNotAvaliableException(newsId);
    }

    @Override
    @Transactional
    public User addUser(NewUserRequest newUserRequest, Role role) {
        User user = userMapper.toUser(newUserRequest);
        if (role.equals(Role.ADMIN)) throw new RightsValidationException("Недостаточно прав для создания администратора.");
        user.setRole(role);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userJPARepository.existsByEmailOrUsername(user.getEmail(), user.getUsername()))
            throw new UniqueDataException("Необходимо использовать другой username и/или e-mail");
        return userJPARepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteUsers(Long userId) {
        User user = userJPARepository.findById(userId).orElseThrow(() -> new UserIsNotFoundException(userId));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new RightsValidationException("Администратор не может быть удален.");
        }
        userJPARepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void banUser(Long userId, UserStatus userStatus) {
       int cnt = userJPARepository.banUser(userStatus, userId);
       if (cnt == 0) throw new UserIsNotFoundException(userId);
    }
}
