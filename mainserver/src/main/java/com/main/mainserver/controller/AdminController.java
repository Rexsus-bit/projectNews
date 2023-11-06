package com.main.mainserver.controller;

import com.main.mainserver.mapper.NewsFullDtoMapper;
import com.main.mainserver.mapper.UserFullDtoMapper;
import com.main.mainserver.model.news.NewsFullDto;
import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.UserFullDto;
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
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {


    private final AdminService adminService;

// TODO    rejectTheNewsById — администратор отклоняет новость, ее требуется скорректировать для повторного согласования
//         approveTheNewsById — администратор одобряет новость, после чего она становится доступна любому пользователю
//         deleteTheNewsById — администратор удаляет новость любого автора
//         createTheUser — создание пользователя
//         deleteTheUser — удаление пользователя
//         createTheAuthor — создание автора
//         deleteTheAuthor — удаление автора

    @PatchMapping("/news/{newsId}/reject")
    public NewsFullDto rejectNews (@PathVariable Long newsId) {
        return NewsFullDtoMapper.INSTANCE.toNewsFullDto(adminService.rejectNews(newsId));
    }

    @PatchMapping("/news/{newsId}/publish")
    public NewsFullDto approveNews(@PathVariable Long newsId) {
        return NewsFullDtoMapper.INSTANCE.toNewsFullDto(adminService.approveNews(newsId));
    }

    @DeleteMapping("/news/{newsId}/user/delete")
    public void deleteNews(@PathVariable Long newsId) {
        adminService.deleteNews(newsId);
    }

    @PostMapping("/users")
    public UserFullDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return UserFullDtoMapper.INSTANCE.toUserFullDto(adminService.addUser(newUserRequest));
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUsers(@PathVariable Long userId) {
        adminService.deleteUsers(userId);
    }

    // TODO Publisher по сути пользователь.

    @PostMapping("/publishers")
    public UserFullDto addPublisher(@Valid @RequestBody NewUserRequest newUserRequest) {
        return UserFullDtoMapper.INSTANCE.toUserFullDto(adminService.addPublisher(newUserRequest));
    }

    @DeleteMapping("/publishers/{userId}")
    public void deletePublisher(@PathVariable Long userId) {
        adminService.deletePublisher(userId);
    }



}
