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
                .name("BANK_USER")
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
        //This user is for /users/{id}/ test
        IcesiUser userForTest = IcesiUser.builder()
                .userId(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"))
                .firstName("User")
                .lastName("Test")
                .password(encoder.encode("password"))
                .email("usertest@email.com")
                .phoneNumber("+573174687853")
                .role(user)
                .build();
        IcesiAccount account1 = IcesiAccount.builder()
                    .id(UUID.randomUUID())
                    .accountNumber("897-887868-67")
                    .balance(10L)
                    .active(true)
                    .type("AHORROS")
                    .user(adminUser)
                    .build();
        IcesiAccount account2 = IcesiAccount.builder()
                .id(UUID.randomUUID())
                .accountNumber("893-887868-67")
                .balance(10L)
                .active(true)
                .type("AHORROS")
                .user(adminUser)
                .build();

        users.save(adminUser);
        users.save(normalUser);
        users.save(bankUser);
        users.save(userForTest);
        return args -> {
            accounts.save(account1);
            accounts.save(account2);
        };
    }
}
