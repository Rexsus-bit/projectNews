package com.main.mainserver.model.user;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserFullDto {

    Long id;
    String email;
    String username;
    Role role;
    UserStatus userStatus;

}
