package com.example.jpa.config;

import com.example.jpa.model.SecurityUser;
import com.example.jpa.security.CustomAuthentication;
import com.example.jpa.service.UserManagementService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IcesiAuthenticatorManager extends DaoAuthenticationProvider {

    public IcesiAuthenticatorManager(UserManagementService userManagementService, PasswordEncoder passwordEncoder) {
        this.setUserDetailsService(userManagementService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken successsAuthentication = (UsernamePasswordAuthenticationToken)
                super.createSuccessAuthentication(principal, authentication, user);
        SecurityUser securityUser = (SecurityUser) user;
        return new CustomAuthentication(successsAuthentication, securityUser.icesiUser().getUserId().toString());
    }


}
