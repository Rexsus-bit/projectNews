package com.main.mainserver.mapper;

import com.main.mainserver.model.news.News;
import com.main.mainserver.model.news.NewsFullDto;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserFullDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserFullDtoMapper {

    UserFullDtoMapper INSTANCE = Mappers.getMapper(UserFullDtoMapper.class);

    public UserFullDto toUserFullDto(User User);

    public List<UserFullDto> toUserFullDtoList(List<User> UsersList);

}
