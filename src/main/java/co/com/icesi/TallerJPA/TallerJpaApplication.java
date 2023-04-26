package co.com.icesi.TallerJPA;

import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TallerJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerJpaApplication.class, args);
	}

	CommandLineRunner commandLineRunner(IcesiUserRepository users,
										IcesiRoleRepository roleRepository,
										PasswordEncoder encoder) {
		IcesiRole icesiRole = IcesiRole.builder()
				.name("prueba")
				.description("prueba de la creación de un role")
				.build();

		IcesiRole icesiRole2 = IcesiRole.builder()
				.name("prueba2")
				.description("prueba de la creación de un role2")
				.build();

		IcesiUser icesiUser = IcesiUser.builder()
				.firstName("Michael")
				.lastName("Jackson")
				.email("jekag85543@marikuza.com")
				.phoneNumber("+5731234567")
				.password("pruebapassword1")
				.role(icesiRole)
				.build();

		IcesiUser icesiUser2 = IcesiUser.builder()
				.firstName("Britney")
				.lastName("Spears")
				.email("padirac613@in2reach.com")
				.phoneNumber("+5790123456")
				.password("pruebapassword2")
				.role(icesiRole2)
				.build();

		return args -> {
			users.save(icesiUser);
			users.save(icesiUser2);
		};
	}
}
