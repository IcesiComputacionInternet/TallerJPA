package co.edu.icesi.tallerjpa.intefrations_test;

import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiPermits;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

public class TestConfigurationData {

    @Bean
    public CommandLineRunner commandLineRunner(
            IcesiUserRepository userRepository,
            IcesiRoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            IcesiAccountRepository accountRepository
    ) {
        return args -> {
            IcesiPermits rolePermission = IcesiPermits.builder()
                    .permitId(UUID.randomUUID())
                    .keyIdentifier("role_admin")
                    .path("/admin/**")
                    .build();

            IcesiPermits userPermission = IcesiPermits.builder()
                    .permitId(UUID.randomUUID())
                    .keyIdentifier("user_admin")
                    .path("/admin/users/**")
                    .build();

            IcesiPermits accountNoEnablePermission = IcesiPermits.builder()
                    .permitId(UUID.randomUUID())
                    .keyIdentifier("account_user")
                    .path("/accounts/disable/**")
                    .build();

            IcesiPermits accountEnablePermission = IcesiPermits.builder()
                    .permitId(UUID.randomUUID())
                    .keyIdentifier("account_user")
                    .path("/accounts/enable/**")
                    .build();

            IcesiPermits accountPermission = IcesiPermits.builder()
                    .permitId(UUID.randomUUID())
                    .keyIdentifier("account_user")
                    .path("/accounts")
                    .build();

            IcesiPermits userBankPermission = IcesiPermits.builder()
                    .permitId(UUID.randomUUID())
                    .keyIdentifier("bank_user")
                    .path("/bank/users/**")
                    .build();

            IcesiRole adminRole = IcesiRole.builder()
                    .roleId(UUID.randomUUID())
                    .description("Administrator Role")
                    .name("ADMIN")
                    .description(List.of(rolePermission, userPermission).toString())
                    .build();

            IcesiRole userRole = IcesiRole.builder()
                    .roleId(UUID.randomUUID())
                    .description("User Role")
                    .name("USER")
                    .description(List.of(accountNoEnablePermission, accountEnablePermission, accountPermission).toString())
                    .build();

            IcesiRole bankRole = IcesiRole.builder()
                    .roleId(UUID.randomUUID())
                    .description("Bank Role")
                    .name("BANK")
                    .description(List.of(userBankPermission).toString())
                    .build();

            IcesiUser admin = IcesiUser.builder()
                    .userId(UUID.randomUUID())
                    .email("admin@example.com")
                    .icesirole(adminRole)
                    .firstName("Jane")
                    .lastName("Smith")
                    .phoneNumber("+123456789")
                    .password(passwordEncoder.encode("adminpassword"))
                    .build();

            IcesiUser user = IcesiUser.builder()
                    .userId(UUID.randomUUID())
                    .email("user@example.com")
                    .icesirole(userRole)
                    .firstName("John")
                    .lastName("Doe")
                    .phoneNumber("+987654321")
                    .password(passwordEncoder.encode("userpassword"))
                    .build();

            IcesiUser user2 = IcesiUser.builder()
                    .userId(UUID.randomUUID())
                    .email("user2@example.com")
                    .icesirole(userRole)
                    .firstName("Alice")
                    .lastName("Johnson")
                    .phoneNumber("+456789012")
                    .password(passwordEncoder.encode("user2password"))
                    .build();

            IcesiUser bank = IcesiUser.builder()
                    .userId(UUID.randomUUID())
                    .email("bank@example.com")
                    .icesirole(bankRole)
                    .firstName("Bob")
                    .lastName("Anderson")
                    .phoneNumber("+987654321")
                    .password(passwordEncoder.encode("bankpassword"))
                    .build();

            IcesiAccount account1 = IcesiAccount.builder()
                    .accountId(UUID.randomUUID())
                    .accountNumber("789")
                    .balance(500)
                    .active(true)
                    .user(user)
                    .build();

            IcesiAccount account2 = IcesiAccount.builder()
                    .accountId(UUID.randomUUID())
                    .accountNumber("987")
                    .balance(1000)
                    .active(true)
                    .user(user2)
                    .build();

            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            roleRepository.save(bankRole);
            userRepository.save(bank);
            userRepository.save(admin);
            userRepository.save(user);
            userRepository.save(user2);
            accountRepository.save(account1);
            accountRepository.save(account2);
        };
    }
}

