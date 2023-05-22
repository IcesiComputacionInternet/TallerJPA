package co.edu.icesi.tallerjpa.runableartefact.integration.controller;

import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@TestConfiguration
public class TestConfigurationData {

    @Bean
    CommandLineRunner commandLineRunner(IcesiUserRepository users,
                                        IcesiRoleRepository roleRepository,
                                        IcesiAccountRepository accountRepository,
                                        PasswordEncoder encoder) {

        IcesiRole icesiRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("ADMIN")
                .build();
        IcesiRole icesiRole2 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("USER")
                .build();
        IcesiRole icesiRole3 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("BANK")
                .build();
        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .role(icesiRole)
                .firstName("John")
                .lastName("Doe")
                .email("test@email.com")
                .phoneNumber("3123342122")
                .password(encoder.encode("password"))
                .build();
        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe2@email.com")
                .role(icesiRole2)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("3123342122")
                .password(encoder.encode("password"))
                .build();
        IcesiUser icesiUser3 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe3@email.com")
                .role(icesiRole3)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("3123342122")
                .password(encoder.encode("password"))
                .build();
        IcesiAccount icesiAccount = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .balance(1000000L)
                .accountNumber("123456789")
                .active(true)
                .type("Ahorros")
                .user(icesiUser)
                .build();
        IcesiAccount icesiAccount2 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .balance(1000000L)
                .accountNumber("987654321")
                .active(true)
                .type("Ahorros")
                .user(icesiUser2)
                .build();
        IcesiAccount icesiAccount3 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .balance(1000000L)
                .accountNumber("1234567890")
                .active(true)
                .type("Ahorros")
                .user(icesiUser3)
                .build();
        IcesiAccount icesiAccount4 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .balance(0L)
                .accountNumber("1122334455")
                .active(true)
                .type("Ahorros")
                .user(icesiUser)
                .build();
        IcesiAccount icesiAccount5 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .balance(0L)
                .accountNumber("1122334456")
                .active(false)
                .type("Ahorros")
                .user(icesiUser)
                .build();
        return args -> {
            roleRepository.save(icesiRole);
            roleRepository.save(icesiRole2);
            roleRepository.save(icesiRole3);
            users.save(icesiUser);
            users.save(icesiUser2);
            users.save(icesiUser3);
            accountRepository.save(icesiAccount);
            accountRepository.save(icesiAccount2);
            accountRepository.save(icesiAccount3);
            accountRepository.save(icesiAccount4);
            accountRepository.save(icesiAccount5);
        };

    }
}
