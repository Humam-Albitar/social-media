package com.humam.social.service;

import com.humam.social.payload.LoginDto;
import com.humam.social.payload.RegisterDto;
import com.humam.social.payload.UserResponse;

public interface UserService {
    UserResponse me();
}
