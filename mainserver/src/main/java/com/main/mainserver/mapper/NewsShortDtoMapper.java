package com.main.mainserver.mapper;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsShortDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NewsShortDtoMapper {

    NewsShortDtoMapper INSTANCE = Mappers.getMapper(NewsShortDtoMapper.class);

    public NewsShortDto toNewsShortDto(News news);

    public List<NewsShortDto> toNewsShortDtoList(List<News> newsList);

}
