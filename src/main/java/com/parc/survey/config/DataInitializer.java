package com.parc.survey.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.parc.survey.domain.User;
import com.parc.survey.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Create a default admin user for testing
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@survey.com");
                admin.setPassword("admin123");
                admin.setRole(User.Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Default admin user created - Username: admin, Password: admin123");
            }
            
            // Create a default regular user for testing
            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setEmail("user@survey.com");
                user.setPassword("user123");
                user.setRole(User.Role.USER);
                userRepository.save(user);
                System.out.println("Default user created - Username: user, Password: user123");
            }
        };
    }
}