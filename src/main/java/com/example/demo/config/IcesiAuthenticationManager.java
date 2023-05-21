package com.example.demo.config;

import com.example.demo.model.SecurityUser;
import com.example.demo.security.CustomAuthentication;
import com.example.demo.service.UserManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.http.HttpServletRequest;

@Component
public class IcesiAuthenticationManager extends DaoAuthenticationProvider {

    public IcesiAuthenticationManager(UserManagementService userManagementService, PasswordEncoder passwordEncoder){
        this.setUserDetailsService(userManagementService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication createSuccessAuthentication(Object princial, Authentication authentication, UserDetails user){
        UsernamePasswordAuthenticationToken successAuthentication = (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(princial,authentication,user);
        SecurityUser securityUser = (SecurityUser) user;
        return new CustomAuthentication(successAuthentication, securityUser.icesiUser().getUserId().toString());
    }
    @Bean
    public AuthorizationManager<RequestAuthorizationContext> requestAuthorizationContextAuthorizationManager
            (HandlerMappingIntrospector instrospector){
        RequestMatcher permitAll = new AndRequestMatcher(new MvcRequestMatcher(instrospector, "/token"));
        RequestMatcherDelegatingAuthorizationManager.Builder managerBuilder = RequestMatcherDelegatingAuthorizationManager.builder()
                .add(permitAll, (context, other) -> new AuthorizationDecision(true));
        managerBuilder.add(new MvcRequestMatcher(instrospector, "/admin/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));
        AuthorizationManager<HttpServletRequest> manager = managerBuilder.build();
        return ((authentication, object) -> manager.check(authentication, object.getRequest()));
    }
}
