package com.main.mainserver.mapper;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsFullDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NewsFullDtoMapper {

    NewsFullDtoMapper INSTANCE = Mappers.getMapper(NewsFullDtoMapper.class);

    public NewsFullDto toNewsFullDto(News news);

    public List<NewsFullDto> toNewsFullDtoList(List<News> newsList);


}
