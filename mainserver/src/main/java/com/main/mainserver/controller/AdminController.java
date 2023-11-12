package com.main.mainserver.controller;


import com.main.mainserver.mapper.NewsMapper;
import com.main.mainserver.mapper.UserMapper;
import com.main.mainserver.model.news.NewsFullDto;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.Role;
import com.main.mainserver.model.user.UserFullDto;

import com.main.mainserver.model.user.UserStatus;
import com.main.mainserver.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserMapper userMapper;

    @PatchMapping("/news/{newsId}/reject")
    public void rejectNews(@PathVariable Long newsId) {
        adminService.rejectNews(newsId);
    }

    @PatchMapping("/news/{newsId}/publish")
    public void approveNews(@PathVariable Long newsId) {
        adminService.approveNews(newsId);
    }

    @DeleteMapping("/news/{newsId}/delete")
    public void deleteNews(@PathVariable Long newsId) {
        adminService.deleteNews(newsId);
    }

    @PostMapping("/users")
    public UserFullDto addUser(@RequestParam Role role,
                               @Valid @RequestBody NewUserRequest newUserRequest) {
        return userMapper.toUserFullDto(adminService.addUser(newUserRequest, role));
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUsers(@PathVariable Long userId) {
        adminService.deleteUsers(userId);
    }

    @PatchMapping("/user/{userId}/ban")
    public void banUser(@PathVariable Long userId,
                        @RequestParam UserStatus userStatus) {
        adminService.banUser(userId, userStatus);
    }


}
