package com.edu.icesi.TallerJPA;

import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
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
	//@Bean
	CommandLineRunner commandLineRunner(UserRepository users,
										RoleRepository roleRepository,
										AccountRepository accounts,
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
		IcesiRole icesiRole3 = IcesiRole.builder()
				.roleId(UUID.randomUUID())
				.description("Role for demo")
				.name("BANK")
				.build();
		IcesiUser icesiUser = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe@email.com")
				.icesiRole(icesiRole)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe2@email.com")
				.icesiRole(icesiRole2)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57314555555")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe3@email.com")
				.icesiRole(icesiRole3)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57123125123")
				.password(encoder.encode("password"))
				.build();
		IcesiAccount icesiAccount = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("000000000")
				.active(true)
				.balance(10000L)
				.type("Normal")
				.icesiUser(icesiUser2)
				.build();
		IcesiAccount icesiAccount2 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("1234567")
				.active(true)
				.balance(15000L)
				.type("Normal")
				.icesiUser(icesiUser2)
				.build();
		IcesiAccount icesiAccount3 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("0246810")
				.active(true)
				.balance(20000L)
				.type("Deposit only")
				.icesiUser(icesiUser2)
				.build();
		IcesiAccount icesiAccount4 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("1357911")
				.active(true)
				.balance(5000L)
				.type("Normal")
				.icesiUser(icesiUser)
				.build();

		return args -> {
			users.save(icesiUser);
			users.save(icesiUser2);
			users.save(icesiUser3);
			accounts.save(icesiAccount);
			accounts.save(icesiAccount2);
			accounts.save(icesiAccount3);
			accounts.save(icesiAccount4);
		};
	}
}


