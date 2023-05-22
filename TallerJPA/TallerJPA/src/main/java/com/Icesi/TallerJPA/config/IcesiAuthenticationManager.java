package com.Icesi.TallerJPA.config;

import com.Icesi.TallerJPA.model.SecurityUser;

import com.Icesi.TallerJPA.security.CustonAuthentication;
import com.Icesi.TallerJPA.service.UserManagementService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class IcesiAuthenticationManager extends DaoAuthenticationProvider {
    public IcesiAuthenticationManager(UserManagementService userManagementService,
                                      PasswordEncoder passwordEncoder) {
        super();
        this.setUserDetailsService(userManagementService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken successAuthentication =
                (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal, authentication, user);
        SecurityUser securityUser = (SecurityUser) user;
        return new CustonAuthentication(successAuthentication, securityUser.icesiUser().getUserId().toString());
    }
}
