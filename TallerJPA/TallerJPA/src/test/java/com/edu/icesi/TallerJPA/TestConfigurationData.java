package com.edu.icesi.TallerJPA;

import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@TestConfiguration
public class TestConfigurationData {

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
				.phoneNumber("+57312444556")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser3 = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("johndoe3@email.com")
				.icesiRole(icesiRole3)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57123456123")
				.password(encoder.encode("password"))
				.build();
		IcesiAccount icesiAccount = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("101111101")
				.active(true)
				.balance(10000L)
				.type("Normal")
				.icesiUser(icesiUser2)
				.build();
		IcesiAccount icesiAccount2 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("1233458")
				.active(true)
				.balance(15000L)
				.type("Normal")
				.icesiUser(icesiUser2)
				.build();
		IcesiAccount icesiAccount3 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("0247910")
				.active(true)
				.balance(25000L)
				.type("Deposit only")
				.icesiUser(icesiUser2)
				.build();
		IcesiAccount icesiAccount4 = IcesiAccount.builder()
				.accountId(UUID.randomUUID())
				.accountNumber("1399119")
				.active(true)
				.balance(5500L)
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
