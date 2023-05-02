package com.example.jpa.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private IcesiAuthenticatorManager icesiAuthenticatorManager;

    private final String secret = "longenoughsecrettotestjwtencrypt";

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(icesiAuthenticatorManager);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> access) throws Exception{
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().access(access)) //permitAll() allows to anybody make a request
                        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] secretBytes = secret.getBytes();
        SecretKeySpec key = new SecretKeySpec(secretBytes, 0, secretBytes.length, "RSA");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>((secret.getBytes())));
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> requestAuthorizationContextAuthorizationManager(HandlerMappingIntrospector introspector) {
        RequestMatcher permitAll = new AndRequestMatcher(new MvcRequestMatcher(introspector, "/token"));

        RequestMatcherDelegatingAuthorizationManager.Builder managerBuilder = RequestMatcherDelegatingAuthorizationManager.builder()
                .add(permitAll, (context, other) -> new AuthorizationDecision(true));
        //These lines give access to the /admin/** path only to users with the SCOPE_ADMIN authority
        managerBuilder.add(new MvcRequestMatcher(introspector, "/admin/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));
        //These lines give acces to the /roles/** path only to users with the SCOPE_ADMIN authority
        managerBuilder.add(new MvcRequestMatcher(introspector, "/roles/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));
        //These lines give acces to the /user/** path only to users with the SCOPE_USER authority
        managerBuilder.add(new MvcRequestMatcher(introspector, "/users/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_USER"));
        //These lines give acces to the /accounts/** path only to users with the SCOPE_USER authority
        managerBuilder.add(new MvcRequestMatcher(introspector, "/accounts/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_USER"));

        AuthorizationManager<HttpServletRequest> manager = managerBuilder.build();
        return (authentication, object) -> manager.check(authentication, object.getRequest());
    }



}
