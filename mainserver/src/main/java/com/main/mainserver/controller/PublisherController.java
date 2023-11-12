package com.main.mainserver.controller;


import com.main.mainserver.mapper.NewsMapper;
import com.main.mainserver.model.news.NewsShortDto;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.security.SecurityUser;
import com.main.mainserver.service.PublisherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping(path = "/publish")
public class PublisherController {

    private final PublisherService publisherService;

    private final NewsMapper newsMapper;

    @PostMapping("/news")
    public NewsShortDto createNews(@Valid @RequestBody NewsRequestDto newsRequestDto,
                                   @AuthenticationPrincipal SecurityUser securityUser,
                                   HttpServletRequest request) {
        return newsMapper.toNewsShortDto(publisherService
                .createNews(newsMapper.toNews(newsRequestDto), securityUser, request));
    }

    // TODO подумать над переопределением имени path variable

    @PatchMapping("/news/{newsId}")
    public NewsShortDto updateNews(@PathVariable Long newsId,
                                   @RequestBody NewsRequestDto newsRequestDto,
                                   @AuthenticationPrincipal SecurityUser securityUser) {
        return newsMapper.toNewsShortDto(publisherService.updateNews(newsId, newsRequestDto, securityUser));
    }

    @DeleteMapping("/news/delete/{newsId}")
    public void deleteNews(@PathVariable Long newsId, @AuthenticationPrincipal  SecurityUser securityUser) {
        publisherService.deleteNews(newsId, securityUser);
    }

}
