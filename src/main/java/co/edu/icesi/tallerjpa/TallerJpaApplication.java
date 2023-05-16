package co.edu.icesi.tallerjpa;

import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class TallerJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerJpaApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository icesiroleRepository,
										PasswordEncoder encoder) {
		IcesiRole icesiRole = IcesiRole.builder()
				.roleId(UUID.fromString("f218a75c-c6af-4f1e-a2c6-b2b47f1a0678"))
				.description("Role for demo")
				.name("ADMIN")
				.build();
		IcesiRole icesiRole2 = IcesiRole.builder()
				.roleId(UUID.fromString("4fd2fef7-e595-48fe-a368-94edd6e142ee"))
				.description("Role for demo")
				.name("USER")
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

		return args -> {
			icesiroleRepository.save(icesiRole);
			icesiroleRepository.save(icesiRole2);
			users.save(icesiUser);
			users.save(icesiUser2);
		};
	}
}
