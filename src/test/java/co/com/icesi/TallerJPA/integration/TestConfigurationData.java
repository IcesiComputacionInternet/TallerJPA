package co.com.icesi.TallerJPA.integration;

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
        IcesiRole icesiRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .build();
        IcesiRole icesiRole2 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("NORMAL")
                .build();

        IcesiRole icesiRole3 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .build();


        IcesiUser icesiUser = IcesiUser.builder()
                .userID(UUID.randomUUID())
                .email("admin@email.com")
                .role(icesiRole)
                .firstName("John")
                .lastName("Doe")
                .password(encoder.encode("password"))
                .build();

        IcesiUser icesiUser2 = IcesiUser.builder()
                .userID(UUID.randomUUID())
                .email("user@email.com")
                .role(icesiRole2)
                .firstName("John")
                .lastName("Doe")
                .password(encoder.encode("password"))
                .build();

        IcesiUser icesiUser3 = IcesiUser.builder()
                .userID(UUID.randomUUID())
                .email("bank@email.com")
                .role(icesiRole2)
                .firstName("John")
                .lastName("Doe")
                .password(encoder.encode("password"))
                .build();


        return args -> {
            roleRepository.save(icesiRole);
            roleRepository.save(icesiRole2);
            roleRepository.save(icesiRole3);
            users.save(icesiUser);
            users.save(icesiUser2);
            users.save(icesiUser3);
        };
    }
}
