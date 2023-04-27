package com.example.jpa;

import com.example.jpa.model.IcesiRole;
import com.example.jpa.model.IcesiUser;
import com.example.jpa.repository.RoleRepository;
import com.example.jpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class JpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaProjectApplication.class, args);
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
				.firstName("John")
				.lastName("Doe")
				.password(encoder.encode("password"))
				.email("johndoe@email.com")
				.phoneNumber("123456789")
				.role(icesiRole)
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.firstName("John")
				.lastName("Doe")
				.password(encoder.encode("password"))
				.email("johndoe2@email.com")
				.phoneNumber("135791113")
				.role(icesiRole2)
				.build();

		return args -> {
			users.save(icesiUser);
			users.save(icesiUser2);
		};
	}
}
