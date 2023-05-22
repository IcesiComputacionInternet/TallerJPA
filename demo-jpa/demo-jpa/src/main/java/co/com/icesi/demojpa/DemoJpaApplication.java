package co.com.icesi.demojpa;

import co.com.icesi.demojpa.model.IcesiPermission;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.PermissionRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserManagementRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DemoJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoJpaApplication.class, args);
	}


	/*@Bean
	CommandLineRunner commandLineRunner(UserManagementRepository users,
										RoleRepository roleRepository,
										PermissionRepository permissionRepository,
										PasswordEncoder encoder) {
		IcesiPermission icesiPermission = IcesiPermission.builder()
				.permissionId(UUID.randomUUID())
				.key("home")
				.path("/home")
				.build();
		IcesiPermission icesiPermission2 = IcesiPermission.builder()
				.permissionId(UUID.randomUUID())
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
		IcesiUser icesiUser = IcesiUser.builder()
				.email("johndoe@email.com")
				.role(icesiRole)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57123123123")
				.password(encoder.encode("password"))
				.build();
		IcesiUser icesiUser2 = IcesiUser.builder()
				.email("johndoe2@email.com")
				.role(icesiRole2)
				.firstName("John")
				.lastName("Doe")
				.phoneNumber("+57321321321")
				.password(encoder.encode("password"))
				.build();

		return args -> {
			users.save(icesiUser);
			users.save(icesiUser2);
		};
	}*/

}
