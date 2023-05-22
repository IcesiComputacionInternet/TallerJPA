package co.edu.icesi.tallerjpa.config;

import co.edu.icesi.tallerjpa.model.IcesiSecurityUser;
import co.edu.icesi.tallerjpa.segurity.CustomAuthentication;
import co.edu.icesi.tallerjpa.service.IcesiUserManagementService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IcesiAuthenticationManager extends DaoAuthenticationProvider {

    private final IcesiUserManagementService userManagementService;

    public IcesiAuthenticationManager(IcesiUserManagementService userManagementService, PasswordEncoder passwordEncoder) {
        super();
        this.userManagementService = userManagementService;
        this.setUserDetailsService(userManagementService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken successAuthentication = (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal, authentication, user);
        IcesiSecurityUser securityUser = (IcesiSecurityUser) user;
        return new CustomAuthentication(successAuthentication, securityUser.icesiUser().getUserId().toString());
    }
}

