package icesi.university.accountSystem;

import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class AccountSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountSystemApplication.class, args);
	}
};
