package com.server.app.dto.auth;

import com.server.app.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.Token;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginResponse {

    private String token;
    private UserResponse data;

}
