package com.edu.icesi.demojpa.integration;
import com.edu.icesi.demojpa.Enum.AccountType;
import com.edu.icesi.demojpa.model.IcesiAccount;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.AccountRepository;
import com.edu.icesi.demojpa.repository.RoleRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@TestConfiguration
public class TestConfigurationData {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository users, AccountRepository accountRepository,
                                        RoleRepository roleRepository,
                                        PasswordEncoder encoder) {
        IcesiRole adminRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .build();
        IcesiRole userRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("USER")
                .build();
        IcesiRole bankRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .build();
        IcesiUser adminUser = IcesiUser.builder()
                .userId(UUID.fromString("c3deb7ba-9b34-4f32-a4ee-caf3ce008fe9"))
                .email("johndoe@email.com")
                .role(adminRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser normalUser = IcesiUser.builder()
                .userId(UUID.fromString("8f3d21fb-eca8-4fa0-9b8f-ec74e8b8030b"))
                .email("johndoe2@email.com")
                .role(userRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser bankUser = IcesiUser.builder()
                .userId(UUID.fromString("7b440cab-245d-4203-a67f-d406eb01a522"))
                .email("johndoe3@email.com")
                .role(bankRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();

        IcesiAccount icesiAccount1 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("000-000000-00")
                .active(true)
                .icesiUser(normalUser)
                .type(AccountType.NORMAL.getType())
                .balance(0)
                .build();

        IcesiAccount secondAccount = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("000-000000-03")
                .active(true)
                .icesiUser(normalUser)
                .type(AccountType.NORMAL.getType())
                .balance(0)
                .build();

        IcesiAccount thirdAccount = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("000-000000-04")
                .active(true)
                .icesiUser(normalUser)
                .type(AccountType.NORMAL.getType())
                .balance(0)
                .build();

        IcesiAccount icesiAccount2 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("000-000000-01")
                .active(true)
                .icesiUser(normalUser)
                .type(AccountType.NORMAL.getType())
                .balance(1000000)
                .build();

        IcesiAccount icesiAccount3 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("000-000000-02")
                .active(true)
                .icesiUser(normalUser)
                .type(AccountType.DEPOSIT_ONLY.getType())
                .balance(1000000)
                .build();



        return args -> {
            users.save(adminUser);
            users.save(normalUser);
            users.save(bankUser);

            accountRepository.save(icesiAccount1);
            accountRepository.save(icesiAccount2);
            accountRepository.save(secondAccount);
            accountRepository.save(thirdAccount);
            accountRepository.save(icesiAccount3);
        };
    }
}