package com.main.mainserver.controller;

import com.main.mainserver.mapper.NewsShortDtoMapper;
import com.main.mainserver.mapper.NewsRequestMapper;
import com.main.mainserver.model.news.NewsShortDto;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.model.news.UpdateNewsRequest;
import com.main.mainserver.service.PublisherService;
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
@RequestMapping(path = "/publish")
public class PublisherController {

    private final PublisherService publisherService;

    @PostMapping("/{publisherId}/news")
    public NewsShortDto createNews(@PathVariable Long publisherId, @Valid @RequestBody NewsRequestDto newsRequestDto) {
        return NewsShortDtoMapper.INSTANCE.toNewsShortDto(publisherService
                .createNews(NewsRequestMapper.INSTANCE.toNews(newsRequestDto), publisherId));
    }

    // TODO подумать над переопределением имени path variable

    @PatchMapping("/{publisherId}/news")
    public NewsShortDto updateNews(@PathVariable Long publisherId, @RequestBody UpdateNewsRequest updateNewsRequest) {
        return NewsShortDtoMapper.INSTANCE.toNewsShortDto(publisherService.updateNews(updateNewsRequest, publisherId));
    }

    @DeleteMapping("/{publisherId}/news/delete/{newsId}")
    public void deleteNews(@PathVariable Long publisherId, @PathVariable Long newsId) {
        publisherService.deleteNews(publisherId, newsId);
    }
}
