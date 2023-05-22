package co.edu.icesi.tallerjpa;

import co.edu.icesi.tallerjpa.model.IcesiPermits;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class RumTallerJPA {

    public static void main(String[] args) {
        SpringApplication.run(RumTallerJPA.class, args);
    }

    @Bean
    @NotNull
    public CommandLineRunner commandLineRunner(IcesiUserRepository users, IcesiRoleRepository role, PasswordEncoder encoder) {

        IcesiPermits rolePermission = IcesiPermits.builder()
                .permitId(UUID.randomUUID())
                .keyIdentifier("admin")
                .path("/role/**")
                .build();

        IcesiPermits userPermission = IcesiPermits.builder()
                .permitId(UUID.randomUUID())
                .keyIdentifier("admin")
                .path("/user/**")
                .build();

        IcesiPermits accountNoEnablePermission = IcesiPermits.builder()
                .permitId(UUID.randomUUID())
                .keyIdentifier("user")
                .path("/account/inactiveAccount/**")
                .build();

        IcesiPermits accountEnablePermission = IcesiPermits.builder()
                .permitId(UUID.randomUUID())
                .keyIdentifier("user")
                .path("/account/activeAccount/**")
                .build();

        IcesiPermits accountPermission = IcesiPermits.builder()
                .permitId(UUID.randomUUID())
                .keyIdentifier("user")
                .path("/account")
                .build();

        IcesiPermits userBankPermission = IcesiPermits.builder()
                .permitId(UUID.randomUUID())
                .keyIdentifier("bank")
                .path("/user/**")
                .build();


        IcesiRole adminRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for administration")
                .name("ADMIN")
                .description(List.of(rolePermission, userPermission).toString())
                .build();

        IcesiRole userRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for regular users")
                .name("USER")
                .description(List.of(accountNoEnablePermission, accountEnablePermission, accountPermission).toString())
                .build();

        IcesiRole bankRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for banks")
                .name("BANK")
                .description(List.of(userBankPermission).toString())
                .build();

        IcesiUser admin = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("admin@example.com")
                .icesirole(adminRole)
                .firstName("Jane")
                .lastName("Smith")
                .phoneNumber("+1234567890")
                .password(encoder.encode("admin123"))
                .build();

        IcesiUser user = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("user@example.com")
                .icesirole(userRole)
                .firstName("Alice")
                .lastName("Johnson")
                .phoneNumber("+9876543210")
                .password(encoder.encode("user123"))
                .build();

        IcesiUser bank = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("bank@example.com")
                .icesirole(bankRole)
                .firstName("Bob")
                .lastName("Williams")
                .phoneNumber("+5555555555")
                .password(encoder.encode("bank123"))
                .build();

        return args -> {
            role.save(adminRole);
            role.save(userRole);
            role.save(bankRole);
            users.save(bank);
            users.save(admin);
            users.save(user);
        };
    }



}
