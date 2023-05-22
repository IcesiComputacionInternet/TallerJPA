package co.edu.icesi.demo;

import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.AccountRepository;
import co.edu.icesi.demo.repository.RoleRepository;
import co.edu.icesi.demo.repository.UserRepository;
import co.edu.icesi.demo.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@TestConfiguration
public class TestConfigurationData {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository users,
                                        RoleRepository roleRepository,
                                        AccountRepository accountRepository,
                                        PasswordEncoder encoder) {

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
        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("Julieta")
                .lastName("Venegas")
                .email("julietav@example.com")
                .phoneNumber("+573184441232")
                .password(encoder.encode("julieta123"))
                .role(admin)
                .build();
        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe2@email.com")
                .phoneNumber("+573123123123")
                .role(user)
                .password(encoder.encode("password"))
                .build();

        IcesiUser icesiUser3 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("icesibank@email.com")
                .phoneNumber("+573123123333")
                .role(bank)
                .password(encoder.encode("password123"))
                .build();

        IcesiUser icesiUser4 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe123@email.com")
                .phoneNumber("+573123123130")
                .role(user)
                .password(encoder.encode("password"))
                .build();
        IcesiAccount icesiAccount1=IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .type("normal")
                .active(true)
                .balance(0)
                .user(icesiUser2)
                .accountNumber("123-123456-21")
                .build();

        IcesiAccount icesiAccount2=IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .type("deposit only")
                .active(true)
                .balance(100)
                .user(icesiUser2)
                .accountNumber("123-123456-22")
                .build();
        IcesiAccount icesiAccount3=IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .type("normal")
                .active(true)
                .balance(1000)
                .user(icesiUser2)
                .accountNumber("123-123456-23")
                .build();
        IcesiAccount icesiAccount4=IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .type("normal")
                .active(true)
                .balance(1000)
                .user(icesiUser4)
                .accountNumber("123-123456-24")
                .build();

        return args -> {
            users.save(icesiUser);
            users.save(icesiUser2);
            users.save(icesiUser3);
            users.save((icesiUser4));
            accountRepository.save(icesiAccount1);
            accountRepository.save(icesiAccount2);
            accountRepository.save(icesiAccount3);
            accountRepository.save(icesiAccount4);
        };
    }
}
