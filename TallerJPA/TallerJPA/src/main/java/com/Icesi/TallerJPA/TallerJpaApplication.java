package com.Icesi.TallerJPA;

import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.repository.IcesiRoleRepository;
import com.Icesi.TallerJPA.repository.IcesiUserRepository;
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
	CommandLineRunner commandLineRunner(IcesiUserRepository users, IcesiRoleRepository roleRepository, PasswordEncoder encoder)
	{
		IcesiRole icesiRole = IcesiRole.builder()
				.description("Role for demo")
				.name("ADMIN")
				.roleId(UUID.randomUUID())
				.build();
		IcesiRole icesiRole2 = IcesiRole.builder()
				.description("Role for demo")
				.name("USER")
				.roleId(UUID.randomUUID())
				.build();
		IcesiRole icesiRole3 = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.description("Role for demo")
				.name("BANK")
				.build();
		IcesiUser icesiUser = IcesiUser.builder()
				.email("johndoe@email.com")
				.firstName("John")
				.icesiRole(icesiRole)
				.lastName("Doe")
				.password(encoder.encode("password"))
				.phoneNumber("+57123123123")
				.userId(UUID.randomUUID())
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.email("johndoe2@email.com")
				.firstName("John")
				.icesiRole(icesiRole2)
				.lastName("Doe")
				.password(encoder.encode("password"))
				.phoneNumber("+57123123123")
				.userId(UUID.randomUUID())
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("ethan@email.com")
				.icesiRole(icesiRole3)
				.firstName("Ethan")
				.lastName("Torchio")
				.phoneNumber("+57320154789")
				.password(encoder.encode("password"))
				.build();
		return args -> {
			roleRepository.save(icesiRole3);
			roleRepository.save(icesiRole2);
			roleRepository.save(icesiRole);
			users.save(icesiUser);
			users.save(icesiUser2);
			users.save(icesiUser3);
		};
	}
}
