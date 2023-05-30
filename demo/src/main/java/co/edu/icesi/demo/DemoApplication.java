package co.edu.icesi.demo;

import co.edu.icesi.demo.enums.TypeAccount;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiAccountRepository;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository roleRepository,
										IcesiAccountRepository accountRepository,
										PasswordEncoder encoder) {


		//UUID idAccount = UUID.randomUUID();
		/*IcesiAccount account1 = new IcesiAccount();
		account1.setAccountId(UUID.randomUUID());
		account1.setAccountNumber("123-456-789");
		account1.setBalance(100);
		account1.setActive(true);
		account1.setType(TypeAccount.ACCOUNT_NORMAL.toString());*/


		/*List<IcesiAccount> accountList = new ArrayList<>();
		accountList.add(account1);*/

		IcesiRole icesiRole = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.roleDescription("Role for demo")
				.roleName("ADMIN")
				.build();
		IcesiRole icesiRole2 = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.roleDescription("Role for demo")
				.roleName("USER")
				.build();
		IcesiRole icesiRole3 = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.roleDescription("Role for demo")
				.roleName("BANK")
				.build();


		IcesiUser icesiUser = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe@email.com")
				.role(icesiRole)
				.firstName("John")
				.lastName("Doe")
				.password(encoder.encode("password"))
				.active(true)
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe2@email.com")
				.role(icesiRole2)
				.firstName("John")
				.lastName("Doe")
				.password(encoder.encode("password"))
				.active(true)
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe3@email.com")
				.role(icesiRole3)
				.firstName("John")
				.lastName("Doe")
				.password(encoder.encode("password"))
				.active(true)
				.build();




		IcesiAccount account1 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.balance(100)
				.accountNumber("123-144256-55")
				.type(TypeAccount.ACCOUNT_NORMAL.toString())
				.active(true)
				.user(icesiUser)
				.build();

		IcesiAccount account2 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.balance(270)
				.accountNumber("666-323232-88")
				.type(TypeAccount.ACCOUNT_NORMAL.toString())
				.active(true)
				.user(icesiUser)
				.build();



		return args -> {
			roleRepository.save(icesiRole);
			roleRepository.save(icesiRole2);
			roleRepository.save(icesiRole3);

			users.save(icesiUser);
			accountRepository.save(account1);
			accountRepository.save(account2);

			users.save(icesiUser2);
			users.save(icesiUser3);

		};
	}
}

