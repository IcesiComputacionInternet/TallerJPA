package co.com.icesi.tallerjpa.integration.configuration;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.model.security.UserPermission;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.PermissionRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.UUID;

@TestConfiguration
public class TestConfigurationData {
    @Bean("commandLineRunner")
    public CommandLineRunner commandLineRunner(UserRepository users,
                                               PermissionRepository permissions,
                                               AccountRepository accounts,
                                               PasswordEncoder encoder) {
        Role adminRole = Role.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .build();
        Role userRole = Role.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("USER")
                .build();
        Role bankUserRole = Role.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK_USER")
                .build();

        UserPermission rolePermission = UserPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("roles")
                .path("/roles/**")
                .roles(List.of(adminRole))
                .build();
        UserPermission accountPermission = UserPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("accounts")
                .path("/accounts/**")
                .roles(List.of(adminRole, userRole))
                .build();
        UserPermission addUserPermission = UserPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("users")
                .path("/users/**")
                .roles(List.of(adminRole, bankUserRole))
                .build();

        IcesiUser admin = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("admin@email.com")
                .role(adminRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser user = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("user@email.com")
                .role(userRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser backUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("bank@email.com")
                .role(bankUserRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();

        Account account = Account.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123456789")
                .balance(1000000L)
                .user(user)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .active(true)
                .build();

        Account account2 = Account.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("987654321")
                .balance(1000000L)
                .user(user)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .active(true)
                .build();

        return args -> {
            users.save(admin);
            users.save(user);
            users.save(backUser);

            permissions.save(rolePermission);
            permissions.save(accountPermission);
            permissions.save(addUserPermission);

            accounts.save(account);
            accounts.save(account2);
        };
    }
}
