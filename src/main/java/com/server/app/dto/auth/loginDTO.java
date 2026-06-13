package com.server.app.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginDTO {

    @NotBlank(message = "username is needed")
    private String username;
    @NotBlank(message = "password is needed")
    private String password;

}
