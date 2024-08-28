package com.humam.social.seeder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class Seeder implements CommandLineRunner {

    @Autowired
    RoleSeeder roleSeeder;

    @Autowired
    UserSeeder userSeeder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        roleSeeder.generate();
        userSeeder.generate();
    }









}
