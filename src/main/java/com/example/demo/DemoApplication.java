package com.example.demo;


import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.IcesiAccount;
import com.example.demo.model.IcesiPermission;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import com.example.demo.model.enums.TypeIcesiAccount;
import com.example.demo.repository.IcesiAccountRepository;
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
										IcesiAccountRepository accountRepository,
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
				.firstName("John")
				.lastName("Doe")
				.email("johndoe@email.com")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.icesiRole(icesiRole)
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.firstName("John")
				.lastName("Doe")
				.email("johndoe2@email.com")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.icesiRole(icesiRole2)
				.build();

		IcesiAccount icesiAccount = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("123-123456-12")
				.balance(1000000)
				.type(TypeIcesiAccount.normal)
				.active(true)
				.icesiUser(icesiUser)
				.build();

		IcesiAccount icesiAccount2 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("123-123456-13")
				.balance(1000000)
				.type(TypeIcesiAccount.normal)
				.active(true)
				.icesiUser(icesiUser)
				.build();
		
		IcesiAccount icesiAccount3 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("123-123456-14")
				.balance(1000000)
				.type(TypeIcesiAccount.normal)
				.active(true)
				.icesiUser(icesiUser)
				.build();
		
		IcesiAccount icesiAccount4 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("123-123456-15")
				.balance(1000000)
				.type(TypeIcesiAccount.normal)
				.active(true)
				.icesiUser(icesiUser2)
				.build();
		
											
		return args -> {
			users.save(icesiUser);
			users.save(icesiUser2);
			accountRepository.save(icesiAccount);
			accountRepository.save(icesiAccount2);
			accountRepository.save(icesiAccount3);
			accountRepository.save(icesiAccount4);
		};
	}
}
