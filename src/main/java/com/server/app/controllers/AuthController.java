package com.server.app.controllers;

import com.server.app.dto.auth.*;
import com.server.app.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<loginResponse> profile(
            @RequestHeader("Authorization") String authorization
    ) {

        String token = authorization.substring(7);

        return ResponseEntity.ok(
                userService.profile(token)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<loginResponse> login(
            @Valid @RequestBody loginDTO request
    ) {

        return ResponseEntity.ok(
                userService.login(request)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<loginResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {

        return ResponseEntity.ok(
                userService.signup(request)
        );
    }


    @PutMapping("/update/password")
    public ResponseEntity<loginResponse> updatePassword(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {

        String token = authorization.substring(7);

        return ResponseEntity.ok(
                userService.updatePassword(token, request)
        );
    }

    @PutMapping("/update/profile")
    public ResponseEntity<loginResponse> updateProfile(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody UpdateProfileRequest request
    ) {

        String token = authorization.substring(7);

        return ResponseEntity.ok(
                userService.updateProfile(token, request)
        );
    }


}
