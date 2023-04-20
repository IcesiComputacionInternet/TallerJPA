package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecutiryConfiguration {
 
    private final IcesiAuthenticatorManager icesiAuthenticatorManager;

    private final String secret = "longenoughsecrettotestjwtencrypt";

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(new DaoAuthenticationProvider());
    }
}
