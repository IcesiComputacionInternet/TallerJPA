package co.com.icesi.demojpa;

import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@TestConfiguration
public class TestConfigurationData {


    @Bean
    public CommandLineRunner commandLineRunner(UserRepository users,
                                               RoleRepository roleRepository,
                                               PasswordEncoder encoder,
                                               AccountRepository accountRepository) {

        IcesiRole admin = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .build();
        IcesiRole bank = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .build();
        IcesiRole user = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("USER")
                .build();
        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe@email.com")
                .role(admin)
                .firstName("John")
                .lastName("Doe")
                .phone("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe2@email.com")
                .role(bank)
                .firstName("John")
                .lastName("Doe")
                .phone("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser icesiUser3 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe3@email,com")
                .role(user)
                .firstName("John")
                .lastName("Doe")
                .phone("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiAccount accountUser1 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("1234567899")
                .balance(1000000)
                .account(icesiUser)
                .active(true)
                .type("polish emissary")
                .build();
        IcesiAccount accountUser3 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123456789")
                .balance(1000000)
                .active(true)
                .type("polish emissary")
                .account(icesiUser3)
                .build();

        return args -> {
            roleRepository.save(admin);
            roleRepository.save(user);
            roleRepository.save(bank);
            users.save(icesiUser);
            users.save(icesiUser2);
            users.save(icesiUser3);
            accountRepository.save(accountUser1);
            accountRepository.save(accountUser3);

            System.out.println("Data loaded");
        };


    }




}
