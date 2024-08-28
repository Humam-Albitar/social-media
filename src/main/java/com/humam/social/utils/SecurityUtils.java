package com.humam.social.utils;

import com.humam.social.entity.User;
import com.humam.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    private static UserRepository userRepository;

    @Autowired
    public SecurityUtils(UserRepository userRepository) {
        SecurityUtils.userRepository = userRepository;
    }
    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
        } else {
            throw new IllegalStateException("User is not authenticated");
        }
    }
}