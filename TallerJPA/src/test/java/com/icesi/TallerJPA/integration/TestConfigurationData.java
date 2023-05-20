package com.icesi.TallerJPA.integration;

import com.icesi.TallerJPA.model.IcesiRole;
import com.icesi.TallerJPA.model.IcesiUser;
import com.icesi.TallerJPA.repository.RoleRepository;
import com.icesi.TallerJPA.repository.UserRespository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class TestConfigurationData {

    @Bean
    CommandLineRunner commandLineRunner(UserRespository users,
                                        RoleRepository roleRepository,
                                        PasswordEncoder encoder) {
        IcesiRole icesiRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .build();
        IcesiRole icesiRole2 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("USER")
                .build();
        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .icesiRole(icesiRole)
                .build();
        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("Gabriel")
                .lastName("Suarez")
                .email("johndoe2@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .icesiRole(icesiRole2)
                .build();
        return args -> {
            roleRepository.save(icesiRole);
            roleRepository.save(icesiRole2);
            users.save(icesiUser);
            users.save(icesiUser2);
        };
    }
}
