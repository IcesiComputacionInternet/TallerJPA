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

        return args -> {
            users.save(icesiUser1);
            users.save(icesiUser2);
            users.save(icesiUser3);
        };
    }
}
