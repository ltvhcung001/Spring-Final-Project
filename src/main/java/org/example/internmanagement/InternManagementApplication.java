package org.example.internmanagement;

import org.example.internmanagement.entity.User;
import org.example.internmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class InternManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                org.example.internmanagement.entity.User admin = new User();
                admin.setFullName("Admin");
                admin.setUsername("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@gmail.com");
                admin.setIsActive(true);
                admin.setRole(User.Role.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}
