package co.edu.icesi.tallerjpa.runableartefact.config;

import co.edu.icesi.tallerjpa.runableartefact.model.SecurityUser;
import co.edu.icesi.tallerjpa.runableartefact.security.CustomAuthentication;
import co.edu.icesi.tallerjpa.runableartefact.service.UserManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IcesiAuthenticatorManager extends DaoAuthenticationProvider {

    public IcesiAuthenticatorManager(UserManagementService userManagementService, PasswordEncoder passwordEncoder) {
        setUserDetailsService(userManagementService);
        setPasswordEncoder(passwordEncoder);

    }
    @Override
    public Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken successAuthentication =
                (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal, authentication, user);
        SecurityUser securityUser = (SecurityUser) user;
        return new CustomAuthentication(successAuthentication, securityUser.icesiUser().getUserId().toString());
    }
}
