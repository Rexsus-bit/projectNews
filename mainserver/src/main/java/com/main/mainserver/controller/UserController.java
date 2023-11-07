package com.main.mainserver.controller;


import com.main.mainserver.mapper.CommentMapper;
import com.main.mainserver.mapper.NewsMapper;
import com.main.mainserver.model.comment.CommentDto;
import com.main.mainserver.model.news.NewsShortDto;
import com.main.mainserver.model.newsApiDto.NewsReportDto;
import com.main.mainserver.security.SecurityUser;
import com.main.mainserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;
    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;


    @GetMapping("/news")
    public List<NewsShortDto> findNews(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Long> usersIdList,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return newsMapper.toNewsShortDtoList(userService.findNews(text, usersIdList, rangeStart, rangeEnd, from, size));
    }

    @GetMapping("/news/external")
    public NewsReportDto getNewsFromWeatherApiService(@RequestParam String query,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        return userService.getNewsFromWeatherApiService(query, from, to);

    }


    @PostMapping("/news/comment") // TODO что должно быть path variable?, Удаление и добавление коммента?
    public CommentDto addCommentToNews(@RequestParam Long newsId,
                                       @RequestParam String commentText,
                                       @AuthenticationPrincipal SecurityUser securityUser) {
        return commentMapper.toCommentDto(userService.addCommentToNews(newsId, commentText, securityUser));
    }

    @DeleteMapping("/news/comment/delete")
    public void deleteCommentToNews(@RequestParam Long commentId,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteCommentToNews(commentId, securityUser);
    }

    @PutMapping("/{newsId}/like")
    public void addLikeFromUser(@PathVariable Long newsId, @AuthenticationPrincipal SecurityUser securityUser) {
        userService.addLikeFromUser(newsId, securityUser);
    }

    @DeleteMapping("/{newsId}/like")
    public void deleteLikeFromUser(@PathVariable Long newsId,
                                   @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteLikeFromUser(newsId, securityUser);
    }

    @GetMapping("/popular")
    public List<NewsShortDto> getTopNews(@RequestParam(defaultValue = "10") Integer limit) {
        return newsMapper.toNewsShortDtoList(userService.getTopNews(limit));
    }

}
