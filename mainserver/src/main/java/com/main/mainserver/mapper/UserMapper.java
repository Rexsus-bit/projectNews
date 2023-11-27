package com.main.mainserver.mapper;

import com.main.mainserver.model.user.NewUserRequest;
import com.main.mainserver.model.user.User;
import com.main.mainserver.model.user.UserFullDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserFullDto toUserFullDto(User User);

    User toUser(NewUserRequest newUserRequest);

}
