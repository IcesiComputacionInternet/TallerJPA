package co.com.icesi.TallerJpa.integration.configuration;

import co.com.icesi.TallerJpa.enums.AccountType;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiAccountRepository;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
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
                                        IcesiAccountRepository accountRepository,
                                        PasswordEncoder encoder){
        IcesiRole adminRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("ADMIN")
                .description("Role for demo")
                .build();
        IcesiRole userRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("USER")
                .description("Role for demo")
                .build();
        IcesiRole bankRole = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("BANK")
                .description("Role for demo")
                .build();

        IcesiUser adminUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.admin@hotmail.com")
                .phoneNumber("3147429867")
                .password(encoder.encode("password"))
                .icesiRole(adminRole)
                .build();
        IcesiUser userUser1 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.user@hotmail.com")
                .phoneNumber("3147429868")
                .password(encoder.encode("password"))
                .icesiRole(userRole)
                .build();
        IcesiUser userUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("Jane")
                .lastName("Doe")
                .email("janedoe.user@hotmail.com")
                .phoneNumber("3157429868")
                .password(encoder.encode("password"))
                .icesiRole(userRole)
                .build();
        IcesiUser bankUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("johndoe.bank@hotmail.com")
                .phoneNumber("3147429869")
                .password(encoder.encode("password"))
                .icesiRole(bankRole)
                .build();
        IcesiAccount account1 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("573-338604-81")
                .balance(200)
                .type(AccountType.NORMAL)
                .active(true)
                .icesiUser(userUser1)
                .build();
        IcesiAccount account2 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("573-338604-82")
                .balance(2000)
                .type(AccountType.DEPOSIT_ONLY)
                .active(true)
                .icesiUser(userUser1)
                .build();
        IcesiAccount account3 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("573-338604-83")
                .balance(150)
                .type(AccountType.NORMAL)
                .active(true)
                .icesiUser(userUser2)
                .build();
        IcesiAccount account4 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("573-338604-84")
                .balance(600)
                .type(AccountType.DEPOSIT_ONLY)
                .active(true)
                .icesiUser(userUser2)
                .build();
        return args -> {
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            roleRepository.save(bankRole);
            users.save(adminUser);
            users.save(userUser1);
            users.save(userUser2);
            users.save(bankUser);
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
            accountRepository.save(account4);
        };
    }
}
