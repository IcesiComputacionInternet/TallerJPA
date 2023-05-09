package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.RoleRepository;
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
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@email.com")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .role(icesiRole)
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
