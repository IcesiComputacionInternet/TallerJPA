package com.example.jpa;

import com.example.jpa.model.IcesiAccount;
import com.example.jpa.model.IcesiRole;
import com.example.jpa.model.IcesiUser;
import com.example.jpa.repository.AccountRepository;
import com.example.jpa.repository.RoleRepository;
import com.example.jpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@TestConfiguration
public class TestConfigurationData {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository users, RoleRepository roleRepository, AccountRepository accounts, PasswordEncoder encoder) {
        IcesiRole admin = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .build();
        IcesiRole user = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("USER")
                .build();
        IcesiRole bank = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .build();
        IcesiUser adminUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .password(encoder.encode("password"))
                .email("johndoe@email.com")
                .phoneNumber("+573174687863")
                .role(admin)
                .build();
        IcesiUser normalUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .password(encoder.encode("password"))
                .email("johndoe2@email.com")
                .phoneNumber("+573174657863")
                .role(user)
                .build();
        IcesiUser bankUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe3@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .role(bank)
                .build();
        IcesiAccount account1 = IcesiAccount.builder()
                    .accountNumber("897-887868-67")
                    .balance(10L)
                    .active(true)
                    .type("AHORROS")
                    .user(adminUser)
                    .build();
        IcesiAccount account2 = IcesiAccount.builder()
                .accountNumber("893-887868-67")
                .balance(10L)
                .active(true)
                .type("AHORROS")
                .user(adminUser)
                .build();
        return args -> {
            roleRepository.save(admin);
            roleRepository.save(user);
            roleRepository.save(bank);
            users.save(adminUser);
            users.save(normalUser);
            users.save(bankUser);
            accounts.save(account1);
            accounts.save(account2);
        };
    }
}
