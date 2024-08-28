package com.humam.social.controller;

import com.humam.social.payload.JWTAuthResponse;
import com.humam.social.payload.LoginDto;
import com.humam.social.payload.RegisterDto;
import com.humam.social.payload.UserResponse;
import com.humam.social.service.AuthService;
import com.humam.social.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(
        name = "X-API-KEY"
)
@SecurityRequirement(
        name = "Bear Authentication"
)
@Tag(
        name = "User APIs For User Resource"
)
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get User Info REST API",
            description = "Get User Info REST API is used to get user info from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping(value = {"/me"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> me(){
        UserResponse response = userService.me();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}