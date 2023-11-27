package com.main.mainserver.model.user;

import lombok.Value;

@Value
public class UserShortDto {

    String username;
    Role role;
    UserStatus userStatus;

}
