package com.humam.social.service;

import com.humam.social.payload.LoginDto;
import com.humam.social.payload.RegisterDto;
import com.humam.social.payload.UserResponse;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);

}
