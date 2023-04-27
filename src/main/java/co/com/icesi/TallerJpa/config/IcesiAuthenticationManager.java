package co.com.icesi.TallerJpa.config;

import co.com.icesi.TallerJpa.model.SecurityUser;
import co.com.icesi.TallerJpa.security.CustomAuthentication;
import co.com.icesi.TallerJpa.service.UserManagementService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IcesiAuthenticationManager extends DaoAuthenticationProvider {
    public  IcesiAuthenticationManager(UserManagementService userManagementService,
                                       PasswordEncoder passwordEncoder){
        this.setUserDetailsService(userManagementService);
        this.setPasswordEncoder(passwordEncoder);
    }

    public Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                      UserDetails userDetails){
        UsernamePasswordAuthenticationToken successAuthentication =
                (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal,
                        authentication, userDetails);
        SecurityUser securityUser = (SecurityUser) userDetails;
        return new CustomAuthentication(successAuthentication, securityUser.icesiUser().getUserId().toString());
    }
}
