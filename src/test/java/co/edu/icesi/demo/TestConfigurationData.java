package co.edu.icesi.demo;

import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.RoleRepository;
import co.edu.icesi.demo.repository.UserRepository;
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
        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("Julieta")
                .lastName("Venegas")
                .email("julietav@example.com")
                .phoneNumber("3184441232")
                .password(encoder.encode("julieta123"))
                .role(icesiRole)
                .build();
        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe2@email.com")
                .phoneNumber("+57123123123")
                .role(icesiRole2)
                .password(encoder.encode("password"))
                .build();

        return args -> {
            users.save(icesiUser);
            users.save(icesiUser2);
        };
    }
}
