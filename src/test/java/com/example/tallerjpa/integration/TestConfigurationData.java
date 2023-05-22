package com.example.tallerjpa.integration;

import com.example.tallerjpa.enums.AccountType;
import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.model.IcesiUser;
import com.example.tallerjpa.repository.AccountRepository;
import com.example.tallerjpa.repository.RoleRepository;
import com.example.tallerjpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class TestConfigurationData {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository users,
                                        RoleRepository roleRepository,
                                        AccountRepository accountRepository,
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
        IcesiRole icesiRole3 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .build();

        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .icesiRole(icesiRole)
                .firstName("John")
                .lastName("Doe")
                .email("juan@hotmail.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .icesiRole(icesiRole2)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe2@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser icesiUser3 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .icesiRole(icesiRole3)
                .firstName("The")
                .lastName("Bank")
                .email("bank@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiAccount account1 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("098-765432-11")
                .balance(1000L)
                .type(AccountType.DEFAULT)
                .active(true)
                .icesiUser(icesiUser)
                .build();

        IcesiAccount account2 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-00")
                .balance(1000L)
                .type(AccountType.DEFAULT)
                .active(true)
                .icesiUser(icesiUser2)
                .build();

        IcesiAccount account3 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("111-567788-33")
                .balance(1000L)
                .type(AccountType.DEPOSIT)
                .active(true)
                .icesiUser(icesiUser3)
                .build();



        return args -> {
            roleRepository.save(icesiRole);
            roleRepository.save(icesiRole2);
            roleRepository.save(icesiRole3);
            users.save(icesiUser);
            users.save(icesiUser2);
            users.save(icesiUser3);
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);

        };
    }
}
