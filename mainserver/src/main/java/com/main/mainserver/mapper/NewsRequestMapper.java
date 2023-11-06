package com.main.mainserver.mapper;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.model.news.NewsStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@Mapper
public interface NewsRequestMapper {

    NewsRequestMapper INSTANCE = Mappers.getMapper(NewsRequestMapper.class);

    public News toNews(NewsRequestDto newsRequestDto);

    @AfterMapping
    default void afterMapping(NewsRequestDto source, @MappingTarget News target) {
        target.setNewsStatus(NewsStatus.CREATED);
        target.setDateTime(LocalDateTime.now());
        target.setLikesList(new HashSet<>());
        target.setComments(new ArrayList<>());
    }

}
