package com.main.mainserver.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Value
@Builder
public class UserShortDto {

    String username;
    Role role;
    UserStatus userStatus;

}
