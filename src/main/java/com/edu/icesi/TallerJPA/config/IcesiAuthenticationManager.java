package com.edu.icesi.TallerJPA.config;

import com.edu.icesi.TallerJPA.model.SecurityUser;
import com.edu.icesi.TallerJPA.security.CustomAuthentication;
import com.edu.icesi.TallerJPA.service.UserManagementService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class IcesiAuthenticationManager extends DaoAuthenticationProvider {

    public IcesiAuthenticationManager(UserManagementService userManagementService, PasswordEncoder passwordEncoderConfiguration){
        this.setUserDetailsService(userManagementService);
        this.setPasswordEncoder(passwordEncoderConfiguration);
    }

    @Override
    public Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails userDetails){
        UsernamePasswordAuthenticationToken successAuthentication = (UsernamePasswordAuthenticationToken)  super.createSuccessAuthentication(principal,
                authentication, userDetails);
        SecurityUser securityUser = (SecurityUser) userDetails;
        return new CustomAuthentication(successAuthentication, securityUser.icesiUser().getUserId().toString());
    }
}
