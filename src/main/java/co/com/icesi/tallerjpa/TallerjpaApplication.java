package co.com.icesi.tallerjpa;

import co.com.icesi.tallerjpa.model.IcesiRole;
import co.com.icesi.tallerjpa.model.IcesiUser;
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

	@Bean
	CommandLineRunner commandLineRunner(UserRepository users,
										RoleRepository roleRepository,
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

		return args -> {
			users.save(icesiUser);
			users.save(icesiUser2);
		};
	}
}
