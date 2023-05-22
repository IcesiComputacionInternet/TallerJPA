package co.com.icesi.tallerjpa;

import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.model.security.UserPermission;
import co.com.icesi.tallerjpa.repository.PermissionRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class TallerJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerJpaApplication.class, args);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner commandLineRunner(UserRepository users,
											   PermissionRepository permissions,
											   PasswordEncoder encoder) {
		Role adminRole = Role.builder()
				.roleId(UUID.randomUUID())
				.description("Role for demo")
				.name("ADMIN")
				.build();
		Role userRole = Role.builder()
				.roleId(UUID.randomUUID())
				.description("Role for demo")
				.name("USER")
				.build();
		Role bankUserRole = Role.builder()
				.roleId(UUID.randomUUID())
				.description("Role for demo")
				.name("BANK_USER")
				.build();

		UserPermission rolePermission = UserPermission.builder()
				.permissionId(UUID.randomUUID())
				.key("roles")
				.path("/roles/**")
				.roles(List.of(adminRole))
				.build();
		UserPermission accountPermission = UserPermission.builder()
				.permissionId(UUID.randomUUID())
				.key("accounts")
				.path("/accounts/**")
				.roles(List.of(adminRole, userRole))
				.build();
		UserPermission addAccountPermission = UserPermission.builder()
				.permissionId(UUID.randomUUID())
				.key("addAccount")
				.path("/accounts/")
				.roles(List.of(bankUserRole))
				.build();
		UserPermission addUserPermission = UserPermission.builder()
				.permissionId(UUID.randomUUID())
				.key("users")
				.path("/users/")
				.roles(List.of(adminRole, bankUserRole))
				.build();

		IcesiUser admin = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("admin@email.com")
				.role(adminRole)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiUser user = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("user@email.com")
				.role(userRole)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiUser backUser = IcesiUser.builder()
				.userId(UUID.randomUUID())
				.email("bank@email.com")
				.role(bankUserRole)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();

		users.save(admin);
		users.save(user);
		users.save(backUser);

		permissions.save(rolePermission);
		permissions.save(accountPermission);
		permissions.save(addUserPermission);
		permissions.save(addAccountPermission);

		return args -> {};
	}
}
