package co.com.icesi.TallerJPA.integration.config;

import co.com.icesi.TallerJPA.Enum.AccountType;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.AccountRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@TestConfiguration
public class TestConfigurationData {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository users,
                                        PasswordEncoder encoder,
                                        AccountRepository accounts) {

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
                .email("johndoe@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .role(admin)
                .build();

        IcesiUser normalUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe2@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
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
                .accountId(UUID.randomUUID())
                .accountNumber("123456789")
                .balance(1000L)
                .type(AccountType.AHORROS)
                .active(true)
                .user(adminUser)
                .build();

        IcesiAccount account2 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("987654321")
                .balance(1000L)
                .type(AccountType.AHORROS)
                .active(true)
                .user(adminUser)
                .build();

        users.save(adminUser);
        users.save(normalUser);
        users.save(bankUser);

        return args -> {
            accounts.save(account1);
            accounts.save(account2);
        };
    }

}
