package co.com.icesi.TallerJPA.config;

import co.com.icesi.TallerJPA.model.SecurityUser;
import co.com.icesi.TallerJPA.security.CustomAuthentication;
import co.com.icesi.TallerJPA.service.UserManagementService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IcesiAuthenticatorManager extends DaoAuthenticationProvider {

    public IcesiAuthenticatorManager(UserManagementService userManagementService, PasswordEncoder passwordEncoder){
        super();
        this.setUserDetailsService(userManagementService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user){
        UsernamePasswordAuthenticationToken successAuthentication = (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal, authentication, user);
        SecurityUser securityUser = (SecurityUser) user;
        return new CustomAuthentication(successAuthentication,securityUser.icesiUser().getUserId().toString());
    }
}
