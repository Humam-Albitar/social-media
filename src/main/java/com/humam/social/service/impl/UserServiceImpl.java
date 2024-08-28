package com.humam.social.service.impl;

import com.humam.social.entity.User;
import com.humam.social.payload.UserResponse;
import com.humam.social.service.UserService;
import com.humam.social.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    public UserServiceImpl() {}

    @Override
    public UserResponse me() {
        User user=SecurityUtils.getCurrentUser();
        UserResponse response=new UserResponse(user.getId(),user.getName(),user.getUsername(),user.getEmail());
        return response;
    }
}
