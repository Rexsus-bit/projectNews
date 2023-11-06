package com.main.mainserver.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFullDto {

    private String name;
    private String email;
    private Role role;
    private LocalDateTime creationDateTime;

}
