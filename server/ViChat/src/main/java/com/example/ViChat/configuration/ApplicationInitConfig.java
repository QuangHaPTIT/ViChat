package com.example.ViChat.configuration;

import com.example.ViChat.constant.RoleStatus;
import com.example.ViChat.entity.Role;
import com.example.ViChat.entity.User;
import com.example.ViChat.repository.RoleRepository;
import com.example.ViChat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_EMAIL = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Init application");
        return args -> {
            if(userRepository.findById(ADMIN_EMAIL).isEmpty()) {
                if(!roleRepository.existsByName(RoleStatus.ADMIN_ROLE)) {
                    Role adminRole = roleRepository.save(
                            Role.builder()
                                    .name(RoleStatus.ADMIN_ROLE)
                                    .description("ROLE_ADMIN")
                                    .build()
                    );

                    roleRepository.save(Role.builder()
                            .name(RoleStatus.USER_ROLE)
                            .description("ROLE_USER")
                            .build());
                    User user = User.builder()
                            .email(ADMIN_EMAIL)
                            .password(passwordEncoder.encode(ADMIN_PASSWORD))
                            .fullName("WangHa")
                            .isActive(true)
                            .roles(Set.of(adminRole))
                            .build();

                    userRepository.save(user);
                    log.warn("admin user has been created with default password: admin, please change it");
                }

            }
            log.info("Application initialization completed .....");
        };
    }
}
