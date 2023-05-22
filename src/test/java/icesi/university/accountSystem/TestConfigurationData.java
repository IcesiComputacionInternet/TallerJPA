package icesi.university.accountSystem;

import icesi.university.accountSystem.enums.TypeAccount;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiAccountRepository;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
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
                                        IcesiAccountRepository icesiAccountRepository,
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
        IcesiRole icesiRole3 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .build();
        IcesiUser icesiUser = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe@gmail.com")
                .role(icesiRole)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+57123123123")
                .password(encoder.encode("password"))
                .build();
        IcesiUser icesiUser2 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe2@email.com")
                .role(icesiRole2)
                .firstName("John")
                .lastName("Doe")
                .password(encoder.encode("password"))
                .phoneNumber("+57323121233")
                .build();
        IcesiUser icesiUser3 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe3@email.com")
                .role(icesiRole3)
                .firstName("John")
                .lastName("Doe")
                .password(encoder.encode("password"))
                .phoneNumber("+57323121235")
                .build();
        IcesiUser icesiUser4 = IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("johndoe4@email.com")
                .role(icesiRole2)
                .firstName("John")
                .lastName("Doe")
                .password(encoder.encode("password"))
                .phoneNumber("+57323121236")
                .build();
        IcesiAccount icesiAccount1 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-90")
                .balance(1000000)
                .user(icesiUser2)
                .active(true)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .build();
        IcesiAccount icesiAccount2 = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-99")
                .balance(1000000)
                .user(icesiUser2)
                .active(true)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .build();
        return args -> {
            roleRepository.save(icesiRole);
            roleRepository.save(icesiRole2);
            roleRepository.save(icesiRole3);
            users.save(icesiUser);
            users.save(icesiUser2);
            users.save(icesiUser3);
            users.save(icesiUser4);
            icesiAccountRepository.save(icesiAccount1);
            icesiAccountRepository.save(icesiAccount2);

        };
    }
}
