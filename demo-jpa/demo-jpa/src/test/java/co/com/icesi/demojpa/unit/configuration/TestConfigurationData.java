package co.com.icesi.demojpa.unit.configuration;


import co.com.icesi.demojpa.enums.IcesiAccountType;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiPermission;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

public class TestConfigurationData {


    @Bean
    public CommandLineRunner commandLineRunner(UserRepository users, RoleRepository role, PasswordEncoder encoder, AccountRepository account) {

        IcesiPermission rolePermission = IcesiPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("admin")
                .path("/role/**")
                .build();

        IcesiPermission userPermission = IcesiPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("admin")
                .path("/user/**")
                .build();

        IcesiPermission accountNoEnablePermission = IcesiPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("user")
                .path("/account/inactiveAccount/**")
                .build();

        IcesiPermission accountEnablePermission = IcesiPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("user")
                .path("/account/activeAccount/**")
                .build();

        IcesiPermission accountPermission = IcesiPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("user")
                .path("/account")
                .build();

        IcesiPermission userBankPermission = IcesiPermission.builder()
                .permissionId(UUID.randomUUID())
                .key("bank")
                .path("/user/**")
                .build();


        IcesiRole adminRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .permissionList(List.of(rolePermission, userPermission))
                .build();

        IcesiRole userRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("USER")
                .permissionList(List.of(accountNoEnablePermission, accountEnablePermission, accountPermission))
                .build();

        IcesiRole bankRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .permissionList(List.of(userBankPermission))
                .build();

        IcesiUser admin = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("admin@email.com")
                .role(adminRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+573226227443")
                .password(encoder.encode("password"))
                .build();

        IcesiUser user = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("user@email.com")
                .role(userRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+573127312289")
                .password(encoder.encode("password"))
                .build();

        IcesiUser user2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("user2@email.com")
                .role(userRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+573121512289")
                .password(encoder.encode("password"))
                .build();

        IcesiUser bank = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("bank@email.com")
                .role(bankRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+573137142007")
                .password(encoder.encode("password"))
                .build();

        IcesiAccount account1 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123")
                .balance(10L)
                .isActive(true)
                .type(IcesiAccountType.DEFAULT)
                .user(user)
                .build();

        IcesiAccount account2 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("456")
                .balance(10L)
                .isActive(true)
                .type(IcesiAccountType.DEFAULT)
                .user(user2)
                .build();

        return args -> {
            role.save(adminRole);
            role.save(userRole);
            role.save(bankRole);
            users.save(bank);
            users.save(admin);
            users.save(user);
            users.save(user2);
            account.save(account1);
            account.save(account2);
        };
    }
}
