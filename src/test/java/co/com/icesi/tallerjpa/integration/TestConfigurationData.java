package co.com.icesi.tallerjpa.integration;

import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
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
                                               PasswordEncoder encoder) {
        Role icesiRole = Role.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .build();
        Role icesiRole2 = Role.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("USER")
                .build();
        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe@email.com")
                .role(icesiRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();

        return args -> {
            users.save(icesiUser);
        };
    }
}
