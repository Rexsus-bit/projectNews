package com.main.mainserver.mapper;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsRequestDto;
import com.main.mainserver.model.news.NewsShortDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, ArrayList.class, HashSet.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    @Mapping(target = "newsStatus", constant = "CREATED")
    @Mapping(target = "dateTime", expression = "java( LocalDateTime.now() )")
    @Mapping(target = "likesSet", expression = "java( new HashSet<>() )")
    @Mapping(target = "commentsList", expression = "java( new ArrayList<>() )")
    News toNews(NewsRequestDto newsRequestDto);

    @Mapping(target = "newsStatus", constant = "CREATED")
    News updateNews(NewsRequestDto newsRequestDto, @MappingTarget News news);

    NewsShortDto toNewsShortDto(News news);

    List<NewsShortDto> toNewsShortDtoList(List<News> newsList);

}
