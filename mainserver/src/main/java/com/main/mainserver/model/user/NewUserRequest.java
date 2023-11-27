package com.main.mainserver.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class NewUserRequest {

    @Email
    String email;
    @NotBlank
    String password;
    @NotBlank
    String username;

}
