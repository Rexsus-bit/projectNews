package com.main.mainserver.mapper;

import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserFullDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    public UserFullDto toUserFullDto(User User);

    public List<UserFullDto> toUserFullDtoList(List<User> UsersList);

    public User toUser(NewUserRequest newUserRequest);

}
