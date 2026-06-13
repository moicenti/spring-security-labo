package com.server.app.dto.auth;

import com.server.app.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Integer id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private Role role;


}
