package com.Icesi.TallerJPA;

import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.repository.IcesiAccountRepository;
import com.Icesi.TallerJPA.repository.IcesiRoleRepository;
import com.Icesi.TallerJPA.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.UUID;
@TestConfiguration
public class TestConfigurationData {
    @Bean
    CommandLineRunner commandLineRunner(IcesiUserRepository users, IcesiRoleRepository roleRepository, IcesiAccountRepository icesiAccountRepository, PasswordEncoder encoder)
    {
        IcesiRole icesiRole = IcesiRole.builder()
                .description("Role for demo")
                .name("ADMIN")
                .roleId(UUID.randomUUID())
                .build();
        IcesiRole icesiRole2 = IcesiRole.builder()
                .description("Role for demo")
                .name("USER")
                .roleId(UUID.randomUUID())
                .build();
        IcesiRole icesiRole3 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .build();
        IcesiAccount icesiAccount = IcesiAccount.builder()
                .accountNumber("12345678")
                .balance(50L)
                .type("Normal")
                .active(true)
                .accountId(UUID.randomUUID())
                .build();
        IcesiUser icesiUser = IcesiUser.builder()
                .email("johndoe@email.com")
                .firstName("John")
                .icesiRole(icesiRole)
                .lastName("Doe")
                .password(encoder.encode("password"))
                .phoneNumber("+57123123123")
                .userId(UUID.randomUUID())
                .build();
        IcesiUser icesiUser2 = IcesiUser.builder()
                .email("johndoe2@email.com")
                .firstName("John")
                .icesiRole(icesiRole2)
                .lastName("Doe")
                .password(encoder.encode("password"))
                .accounts(Collections.singletonList(icesiAccount))
                .phoneNumber("+57123123123")
                .userId(UUID.randomUUID())
                .build();
        IcesiUser icesiUser3 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("ethan@email.com")
                .icesiRole(icesiRole3)
                .firstName("Ethan")
                .lastName("Torch")
                .phoneNumber("+57320154789")
                .password(encoder.encode("password"))
                .build();
        return args -> {
            roleRepository.save(icesiRole3);
            roleRepository.save(icesiRole2);
            roleRepository.save(icesiRole);
            icesiAccountRepository.save(icesiAccount);
            users.save(icesiUser);
            users.save(icesiUser2);
            users.save(icesiUser3);
        };
    }
}
