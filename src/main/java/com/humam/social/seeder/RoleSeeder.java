package com.humam.social.seeder;


import com.humam.social.repository.RoleRepository;
import com.humam.social.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RoleSeeder implements BaseSeeder{


    @Autowired
    private RoleRepository roleRepository;

    public void generate() throws IOException {
        addRolesAndRoleBasedTopics();
    }
    private void addRolesAndRoleBasedTopics() {
        if(roleRepository.count() <= 0) {
            List<Role> rs= new ArrayList<>();
            Role r1 = new Role();
            r1.setName("ROLE_ADMIN");
            rs.add(r1);
            Role r2 = new Role();
            r2.setName("ROLE_USER");
            rs.add(r2);
            roleRepository.saveAll(rs);
            log.info("Roles table seeded");
        }else {
            log.trace("Roles Seeding Not Required");
        }
    }
}
