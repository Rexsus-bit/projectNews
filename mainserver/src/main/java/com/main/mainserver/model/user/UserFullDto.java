package com.main.mainserver.model.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;


@Value
@Builder
public class UserFullDto {

    Long id;
    String email;
    String password;
    String username;
    Role role;
    UserStatus userStatus;

}
