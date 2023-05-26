package co.com.icesi.jpataller.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final IcesiAuthenticatorManager icesiAuthenticatorManager;

    private final PermissionRepository joemama;



}
