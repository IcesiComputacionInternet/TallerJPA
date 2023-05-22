package co.com.icesi.tallerjpa;

import co.com.icesi.tallerjpa.Enum.AccountType;
import co.com.icesi.tallerjpa.model.IcesiAccount;
import co.com.icesi.tallerjpa.model.IcesiRole;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class TallerjpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerjpaApplication.class, args);
	}
	//@Bean
	CommandLineRunner commandLineRunner(UserRepository users,
										AccountRepository accounts,
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
				.email("johndoe@email.com")
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
				.email("janedoe14@email.com")
				.role(icesiRole3)
				.firstName("Jane")
				.lastName("Doe")
				.phoneNumber("+57123123100")
				.password(encoder.encode("password"))
				.build();
		IcesiAccount account1 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("7654321")
				.balance(50000L)
				.type(AccountType.NORMAL.toString())
				.user(icesiUser2)
				.active(true)
				.build();
		IcesiAccount account2 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("1234567")
				.balance(50000L)
				.type(AccountType.DEPOSIT_ONLY.toString())
				.user(icesiUser2)
				.active(true)
				.build();
		IcesiAccount account3 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("2468910")
				.balance(0L)
				.type(AccountType.NORMAL.toString())
				.user(icesiUser2)
				.active(true)
				.build();

		return args -> {
			users.save(icesiUser);
			users.save(icesiUser2);
			users.save(icesiUser3);
			accounts.save(account1);
			accounts.save(account2);
			accounts.save(account3);
		};
	}
}
