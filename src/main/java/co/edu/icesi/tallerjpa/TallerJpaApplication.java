package co.edu.icesi.tallerjpa;

import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class TallerJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerJpaApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository icesiroleRepository,
										IcesiAccountRepository icesiAccountRepository,
										PasswordEncoder encoder) {
		IcesiRole icesiRole = IcesiRole.builder()
				.roleId(UUID.fromString("f218a75c-c6af-4f1e-a2c6-b2b47f1a0678"))
				.description("Role for demo")
				.name(NameIcesiRole.ADMIN.toString())
				.build();
		IcesiRole icesiRole2 = IcesiRole.builder()
				.roleId(UUID.fromString("4fd2fef7-e595-48fe-a368-94edd6e142ee"))
				.description("Role for demo")
				.name(NameIcesiRole.USER.toString())
				.build();
		IcesiRole icesiRole3 = IcesiRole.builder()
				.roleId(UUID.fromString("3fd2fef7-e595-48fe-a368-94edd6e142ee"))
				.description("Role for demo")
				.name(NameIcesiRole.BANK.toString())
				.build();
		IcesiUser icesiUser = IcesiUser.builder()
				.userId(UUID.fromString("04dacbf5-4815-4d6e-a2a7-db01a607f237"))
				.icesiRole(icesiRole)
				.firstName("John")
				.lastName("Doe")
				.email("johndoe@email.com")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.userId(UUID.fromString("df17e266-dcc4-4bf2-923c-bb5559722f50"))
				.firstName("John")
				.lastName("Doe")
				.email("johndoe2@email.com")
				.icesiRole(icesiRole2)
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.fromString("af17e266-dcc4-4bf2-923c-bb5559722f50"))
				.firstName("John")
				.lastName("Doe")
				.email("johndoe3@email.com")
				.icesiRole(icesiRole3)
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiAccount icesiAccount = IcesiAccount.builder()
				.accountId(UUID.fromString("aa14c92e-7505-4fe3-8bb7-2f418504e867"))
				.accountNumber("222-123456-22")
				.balance(1500)
				.type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
				.active(true)
				.icesiUser(icesiUser)
				.build();
		IcesiAccount icesiAccount2 = IcesiAccount.builder()
				.accountId(UUID.fromString("cc62238e-ce1b-4026-9ab5-2f47944dd150"))
				.accountNumber("111-123456-11")
				.balance(650)
				.type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
				.active(true)
				.icesiUser(icesiUser2)
				.build();
		IcesiAccount icesiAccount3 = IcesiAccount.builder()
				.accountId(UUID.fromString("0b4251be-3aed-46fb-82cc-49d1f8991f6c"))
				.accountNumber("333-123456-33")
				.balance(1500)
				.type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
				.active(false)
				.icesiUser(icesiUser)
				.build();
		IcesiAccount icesiAccount4 = IcesiAccount.builder()
				.accountId(UUID.fromString("a80da076-8195-4ff1-acb5-3ba2d5dd44dc"))
				.accountNumber("444-123456-44")
				.balance(650)
				.type(TypeIcesiAccount.DEPOSIT_ONLY.toString())
				.active(true)
				.icesiUser(icesiUser2)
				.build();

		return args -> {
			icesiroleRepository.save(icesiRole);
			icesiroleRepository.save(icesiRole2);
			icesiroleRepository.save(icesiRole3);
			users.save(icesiUser);
			users.save(icesiUser2);
			users.save(icesiUser3);
			icesiAccountRepository.save(icesiAccount);
			icesiAccountRepository.save(icesiAccount2);
			icesiAccountRepository.save(icesiAccount3);
			icesiAccountRepository.save(icesiAccount4);
		};
	}
}
