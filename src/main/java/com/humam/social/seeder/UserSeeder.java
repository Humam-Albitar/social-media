package com.humam.social.seeder;


import com.humam.social.repository.RoleRepository;
import com.humam.social.repository.UserRepository;
import com.humam.social.entity.Role;
import com.humam.social.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class UserSeeder implements BaseSeeder{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void generate() throws IOException {
        addSuperAdmin();
    }

    private void addSuperAdmin() {
        if(userRepository.count() <= 0) {
            User admin = new User();
            admin.setName("admin");
            admin.setEmail("admin@email.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            Role adminRole=roleRepository.findByName("ROLE_ADMIN").orElse(null);
            if(adminRole!=null)
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
            log.info("User table seeded");
        }else {
            log.trace("User Seeding Not Required");
        }
    }

}
