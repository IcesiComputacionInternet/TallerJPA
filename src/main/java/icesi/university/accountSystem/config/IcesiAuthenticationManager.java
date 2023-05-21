package icesi.university.accountSystem.config;

import icesi.university.accountSystem.model.SecurityUser;
import icesi.university.accountSystem.security.CustomAuthentication;
import icesi.university.accountSystem.services.TokenService;
import icesi.university.accountSystem.services.UserManagementService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IcesiAuthenticationManager extends DaoAuthenticationProvider {
    public IcesiAuthenticationManager(UserManagementService userManagementService, PasswordEncoder passwordEncoder){
        this.setUserDetailsService(userManagementService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user){
        UsernamePasswordAuthenticationToken succesAuthentication = (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal,authentication, user);
        SecurityUser securityUser = (SecurityUser) user;
        return new CustomAuthentication(succesAuthentication, securityUser.icesiUser().getUserId().toString());
    }
}
