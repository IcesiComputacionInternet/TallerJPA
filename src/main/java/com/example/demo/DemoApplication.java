package com.example.demo;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.IcesiPermission;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import com.example.demo.repository.IcesiRoleRepository;
import com.example.demo.repository.IcesiUserRepository;
import com.example.demo.repository.PermissionRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository roleRepository,
										PermissionRepository permissionRepository,
										PasswordEncoder encoder) {
		IcesiPermission icesiPermission = IcesiPermission.builder()
				.key("home")
				.path("/home")
				.build();
		IcesiPermission icesiPermission2 = IcesiPermission.builder()
				.key("admin")
				.path("/admin")
				.build();
		icesiPermission = permissionRepository.save(icesiPermission);
		icesiPermission2 = permissionRepository.save(icesiPermission2);
		IcesiRole icesiRole = IcesiRole.builder()
				.description("Role for demo")
				.name("ADMIN")
				.permissionList(List.of(icesiPermission, icesiPermission2))
				.build();
		IcesiRole icesiRole2 = IcesiRole.builder()
				.description("Role for demo")
				.name("USER")
				.permissionList(List.of(icesiPermission))
				.build();
		icesiRole = roleRepository.save(icesiRole);
		icesiRole2 = roleRepository.save(icesiRole2);
		IcesiUser icesiUser = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.firstName("John")
				.lastName("Doe")
				.email("johndoe@email.com")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.icesiRole(icesiRole)
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.firstName("John")
				.lastName("Doe")
				.email("johndoe2@email.com")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.icesiRole(icesiRole2)
				.build();
											
		return args -> {
			users.save(icesiUser);
			users.save(icesiUser2);
		};
	}

}
