package co.com.icesi.icesiAccountSystem;

import co.com.icesi.icesiAccountSystem.enums.AccountType;
import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.AccountRepository;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class IcesiAccountSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(IcesiAccountSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository
			roleRepository, AccountRepository accountRepository, PasswordEncoder encoder) {
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
				.name("BANK_USER")
				.build();
		IcesiUser icesiUser = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe@email.com")
				.role(icesiRole)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("keren@email.com")
				.role(icesiRole2)
				.firstName("Keren")
				.lastName("Lopez")
				.phoneNumber("+57123123128")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("ethan@email.com")
				.role(icesiRole3)
				.firstName("Ethan")
				.lastName("Torchio")
				.phoneNumber("+57320154789")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser4 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("luis@email.com")
				.role(icesiRole2)
				.firstName("Luis")
				.lastName("Torres")
				.phoneNumber("+57320154787")
				.password(encoder.encode("password"))
				.build();
		IcesiAccount icesiAccount = IcesiAccount.builder()
				.accountNumber("025-253568-54")
				.active(true)
				.accountId(UUID.randomUUID())
                .user(icesiUser2)
                .balance(0)
                .type(AccountType.DEPOSIT_ONLY)
                .build();
		IcesiAccount icesiAccount2 = IcesiAccount.builder()
				.accountNumber("025-253568-33")
				.active(true)
				.accountId(UUID.randomUUID())
				.user(icesiUser4)
				.balance(25000000)
				.type(AccountType.NORMAL)
				.build();
        IcesiAccount icesiAccount3 = IcesiAccount.builder()
				.accountNumber("025-253568-25")
				.active(true)
				.accountId(UUID.randomUUID())
                .user(icesiUser2)
                .balance(50000000)
                .type(AccountType.NORMAL)
                .build();

		return args -> {
			userRepository.save(icesiUser);
			userRepository.save(icesiUser2);
			userRepository.save(icesiUser3);
			userRepository.save(icesiUser4);
			accountRepository.save(icesiAccount);
			accountRepository.save(icesiAccount2);
			accountRepository.save(icesiAccount3);
		};
	}
}
