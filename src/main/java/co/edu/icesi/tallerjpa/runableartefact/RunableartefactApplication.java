package co.edu.icesi.tallerjpa.runableartefact;

import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class RunableartefactApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunableartefactApplication.class, args);
	}

	/*
	@Bean
	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository roleRepository,
										PasswordEncoder encoder) {

		IcesiRole icesiRole = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.name("ADMIN")
				.build();
		IcesiRole icesiRole2 = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.name("USER")
				.build();
		IcesiRole icesiRole3 = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.name("BANK")
				.build();
		IcesiUser icesiUser = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.role(icesiRole)
				.firstName("John")
				.lastName("Doe")
				.email("test@email.com")
				.phoneNumber("3123342122")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe2@email.com")
				.role(icesiRole2)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("3123342122")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe3@email.com")
				.role(icesiRole3)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("3123342122")
				.password(encoder.encode("password"))
				.build();
		return args -> {
			roleRepository.save(icesiRole);
			roleRepository.save(icesiRole2);
			roleRepository.save(icesiRole3);
			users.save(icesiUser);
			users.save(icesiUser2);
			users.save(icesiUser3);
		};

	} */
}
