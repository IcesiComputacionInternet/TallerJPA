package co.com.icesi.TallerJPA;

import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
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
                                        PasswordEncoder encoder) {
        IcesiRole icesiRole1 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("ADMIN")
                .description("Role for demo")
                .build();

        IcesiRole icesiRole2 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("USER")
                .description("Role for demo")
                .build();

        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .role(icesiRole1)
                .build();

        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe2@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .role(icesiRole2)
                .build();

        return args -> {
            users.save(icesiUser);
            users.save(icesiUser2);
        };
    }
}
