package com.example.demo;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import com.example.demo.repository.IcesiRoleRepository;
import com.example.demo.repository.IcesiUserRepository;

@TestConfiguration
public class TestConfigurationData {
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
