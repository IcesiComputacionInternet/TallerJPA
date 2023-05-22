package icesi.university.accountSystem;

import icesi.university.accountSystem.enums.TypeAccount;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiAccountRepository;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class AccountSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountSystemApplication.class, args);
	}
	@Bean
	@Profile("!test")
	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository roleRepository,
										IcesiAccountRepository accountRepository,
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
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe3@email.com")
				.role(icesiRole3)
				.firstName("John")
				.lastName("Bank")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiAccount icesiAccount = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("123-456789-90")
				.balance(1000000)
				.user(icesiUser2)
				.active(true)
				.balance(0)
				.type(TypeAccount.ACCOUNT_NORMAL)
				.build();
		return args -> {
			roleRepository.save(icesiRole);
			roleRepository.save(icesiRole2);
			roleRepository.save(icesiRole3);
			users.save(icesiUser);
			users.save(icesiUser2);
			users.save(icesiUser3);
			accountRepository.save(icesiAccount);
		};
	}

}
