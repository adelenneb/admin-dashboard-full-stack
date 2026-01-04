package com.example.adminapi.config;

import com.example.adminapi.domain.Role;
import com.example.adminapi.domain.User;
import com.example.adminapi.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

  private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

  @Bean
  CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      if (userRepository.existsByUsername("admin")) {
        return;
      }

      User admin = User.builder()
        .username("admin")
        .email("admin@example.com")
        .password(passwordEncoder.encode("admin123"))
        .role(Role.ADMIN)
        .build();

      userRepository.save(admin);
      log.info("Seeded admin user -> username: admin, password: admin123 (dev only)");
    };
  }
}
