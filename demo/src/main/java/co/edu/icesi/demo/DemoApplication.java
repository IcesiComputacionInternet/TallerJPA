package co.edu.icesi.demo;

import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository roleRepository,
										PasswordEncoder encoder) {
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
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe2@email.com")
				.role(icesiRole2)
				.firstName("John")
				.lastName("Doe")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe3@email.com")
				.role(icesiRole3)
				.firstName("John")
				.lastName("Doe")
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
	}
}

