package com.ng.oauth2.demo.security.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadUsersInDatabase {

    @Value("${data.users:admin,mgr,user}")
    private String[] users;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initUsers(UserRepository repo) {

        return args -> {
            for (int i = 0; i < users.length; i++) {
                String email = users[i];//+ "@" + users[i] + ".com";
                User.Role role = i > 1 ? User.Role.ROLE_USER : i == 0 ? User.Role.ROLE_ADMIN : User.Role.ROLE_USER_MANAGER;
                String pwd = passwordEncoder.encode("pwd");
                log.info("save {}", repo.save(new User(null, email, pwd, role)));
            }
        };
    }
	

}
