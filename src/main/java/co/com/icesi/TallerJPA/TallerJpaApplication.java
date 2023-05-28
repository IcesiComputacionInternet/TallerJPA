package co.com.icesi.TallerJPA;

import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
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

	/**
	 *
	 * @Bean
	 *     CommandLineRunner commandLineRunner(IcesiUserRepository users,
	 *                                         IcesiRoleRepository roleRepository,
	 *             IcesiAccountRespository accountRespository,
	 *                                         PasswordEncoder encoder) {
	 *         IcesiRole icesiRole = IcesiRole.builder()
	 *                 .roleId(UUID.randomUUID())
	 *                 .description("Role for demo")
	 *                 .name("ADMIN")
	 *                 .build();
	 *         IcesiRole icesiRole2 = IcesiRole.builder()
	 *                 .roleId(UUID.randomUUID())
	 *                 .description("Role for demo")
	 *                 .name("NORMAL")
	 *                 .build();
	 *
	 *         IcesiRole icesiRole3 = IcesiRole.builder()
	 *                 .roleId(UUID.randomUUID())
	 *                 .description("Role for demo")
	 *                 .name("BANK")
	 *                 .build();
	 *
	 *
	 *         IcesiUser icesiUser = IcesiUser.builder()
	 *                 .userID(UUID.randomUUID())
	 *                 .email("admin@email.com")
	 *                 .role(icesiRole)
	 *                 .firstName("John")
	 *                 .lastName("Doe")
	 *                 .password(encoder.encode("password"))
	 *                 .build();
	 *
	 *         IcesiUser icesiUser2 = IcesiUser.builder()
	 *                 .userID(UUID.randomUUID())
	 *                 .email("user@email.com")
	 *                 .role(icesiRole2)
	 *                 .firstName("John")
	 *                 .lastName("Doe")
	 *                 .password(encoder.encode("password"))
	 *                 .build();
	 *
	 *         IcesiUser icesiUser3 = IcesiUser.builder()
	 *                 .userID(UUID.randomUUID())
	 *                 .email("bank@email.com")
	 *                 .role(icesiRole3)
	 *                 .firstName("John")
	 *                 .lastName("Doe")
	 *                 .password(encoder.encode("password"))
	 *                 .build();
	 *
	 *         IcesiUser icesiUser4 = IcesiUser.builder()
	 *                 .userID(UUID.randomUUID())
	 *                 .email("user2@email.com")
	 *                 .role(icesiRole2)
	 *                 .firstName("John")
	 *                 .lastName("Doe")
	 *                 .password(encoder.encode("password"))
	 *                 .build();
	 *
	 *         IcesiAccount account1 = IcesiAccount.builder()
	 *                 .accountId(UUID.randomUUID())
	 *                 .accountNumber("903-178442-08")
	 *                 .balance(1000L)
	 *                 .type("NORMAL")
	 *                 .active(true)
	 *                 .user(icesiUser)
	 *                 .build();
	 *
	 *         IcesiAccount account2 = IcesiAccount.builder()
	 *                 .accountId(UUID.randomUUID())
	 *                 .accountNumber("956-648065-61")
	 *                 .balance(1000L)
	 *                 .type("NORMAL")
	 *                 .active(true)
	 *                 .user(icesiUser2)
	 *                 .build();
	 *
	 *         IcesiAccount account3 = IcesiAccount.builder()
	 *                 .accountId(UUID.randomUUID())
	 *                 .accountNumber("956-648065-62")
	 *                 .balance(1000L)
	 *                 .type("DEPOSIT")
	 *                 .active(true)
	 *                 .user(icesiUser4)
	 *                 .build();
	 *
	 *         return args -> {
	 *             roleRepository.save(icesiRole);
	 *             roleRepository.save(icesiRole2);
	 *             roleRepository.save(icesiRole3);
	 *
	 *             users.save(icesiUser);
	 *             users.save(icesiUser2);
	 *             users.save(icesiUser3);
	 *             users.save(icesiUser4);
	 *
	 *             accountRespository.save(account1);
	 *             accountRespository.save(account2);
	 *             accountRespository.save(account3);
	 *         };
	 *     }
	 *
	 *
	 *
	 *
	 *
	 *
	 *
	 */

}
