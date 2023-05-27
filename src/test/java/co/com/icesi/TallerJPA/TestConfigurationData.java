package co.com.icesi.TallerJPA;

import co.com.icesi.TallerJPA.enums.AccountType;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRepository;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
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
                                        IcesiAccountRepository accounts,
                                        PasswordEncoder encoder) {
        IcesiRole icesiRole1 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("ADMIN")
                .description("Role admin for demo")
                .build();

        IcesiRole icesiRole2 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("USER")
                .description("Role user for demo")
                .build();

        IcesiRole icesiRole3 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("BANK")
                .description("Role bank for demo")
                .build();

        IcesiUser icesiUser1 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe1@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .role(icesiRole1)
                .build();

        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe2@email.com")
                .phoneNumber("+57123123145")
                .password(encoder.encode("password"))
                .role(icesiRole2)
                .build();

        IcesiUser icesiUser3 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe3@email.com")
                .phoneNumber("+57123166345")
                .password(encoder.encode("password"))
                .role(icesiRole3)
                .build();

        IcesiAccount account1= IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("799-948879-27")
                .balance(100L)
                .accountType(AccountType.STANDARD_ACCOUNT)
                .active(true)
                .icesiUser(icesiUser2)
                .build();

        IcesiAccount account2= IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("452-976314-32")
                .balance(100L)
                .accountType(AccountType.STANDARD_ACCOUNT)
                .active(true)
                .icesiUser(icesiUser2)
                .build();

        IcesiAccount account3= IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("799-245879-27")
                .balance(100L)
                .accountType(AccountType.DEPOSIT_ONLY)
                .active(true)
                .icesiUser(icesiUser2)
                .build();

        IcesiAccount account4= IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("799-948879-40")
                .balance(0L)
                .accountType(AccountType.STANDARD_ACCOUNT)
                .active(true)
                .icesiUser(icesiUser2)
                .build();

        IcesiAccount account5= IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("98974629")
                .balance(0L)
                .accountType(AccountType.STANDARD_ACCOUNT)
                .active(true)
                .icesiUser(icesiUser2)
                .build();

        return args -> {
            users.save(icesiUser1);
            users.save(icesiUser2);
            users.save(icesiUser3);
            accounts.save(account1);
            accounts.save(account2);
            accounts.save(account3);
            accounts.save(account4);
            accounts.save(account5);
        };
    }
}
