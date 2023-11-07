package com.main.mainserver.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Value
@Builder
public class NewUserRequest {

    @Email
    String email;
    @NotBlank
    String password;
    @NotBlank
    String username;

}
