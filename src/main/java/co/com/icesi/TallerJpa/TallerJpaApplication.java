package co.com.icesi.TallerJpa;

import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class TallerJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerJpaApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository roleRepository,
										PasswordEncoder encoder) {
		IcesiRole roleAdmin = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.name("ADMIN")
				.description("Role for demo")
				.build();
		IcesiRole roleUser = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.name("USER")
				.description("Role for demo")
				.build();
		IcesiRole roleBank = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.name("BANK")
				.description("Role for demo")
				.build();

		IcesiUser userAdmin = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.firstName("John")
				.lastName("Doe")
				.email("johndoe@email.com")
				.phoneNumber("+57323123123")
				.password(encoder.encode("password"))
				.icesiRole(roleAdmin)
				.build();
		IcesiUser userNormal = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.firstName("John")
				.lastName("Doe")
				.email("johndoe2@email.com")
				.phoneNumber("+57323123124")
				.password(encoder.encode("password"))
				.icesiRole(roleUser)
				.build();
		IcesiUser userBank = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.firstName("John")
				.lastName("Doe")
				.email("johndoe3@email.com")
				.phoneNumber("+57323123125")
				.password(encoder.encode("password"))
				.icesiRole(roleBank)
				.build();

		return args -> {
			roleRepository.save(roleAdmin);
			roleRepository.save(roleUser);
			roleRepository.save(roleBank);
			users.save(userAdmin);
			users.save(userNormal);
			users.save(userBank);
		};
	}
}
