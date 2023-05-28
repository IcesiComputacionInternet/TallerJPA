package co.edu.icesi.tallerjpa.config;

import co.edu.icesi.tallerjpa.repository.IcesiPermissionRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final IcesiAuthenticationManager authenticationManager;

    private final String jwtSecret = "longenoughsecrettotestjwtencrypt";

    private final IcesiPermissionRepository permissionRepository;

    @Bean
    public AuthenticationManager customAuthenticationManager() {
        return new ProviderManager(authenticationManager);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> accessManager) throws Exception {
        return http
                .cors()
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().access(accessManager))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] secretBytes = jwtSecret.getBytes();
        SecretKeySpec key = new SecretKeySpec(secretBytes, 0, secretBytes.length, "RSA");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecret.getBytes()));
    }

    @Bean
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public AuthorizationManager<RequestAuthorizationContext> requestMatcherAuthorizationManager
            (HandlerMappingIntrospector introspector) {
        RequestMatcher permitAll = new AndRequestMatcher(new MvcRequestMatcher(introspector, "/token"));

        RequestMatcherDelegatingAuthorizationManager.Builder managerBuilder
                = RequestMatcherDelegatingAuthorizationManager.builder()
                .add(permitAll, (context, other) -> new AuthorizationDecision(true));

        managerBuilder.add(new MvcRequestMatcher(introspector, "/admin/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));
        managerBuilder.add(new MvcRequestMatcher(introspector, "/user"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_USER"));
        managerBuilder.add(new MvcRequestMatcher(introspector, "/role"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_BANK"));


        AuthorizationManager<HttpServletRequest> manager = managerBuilder.build();
        return (authentication, object) -> manager.check(authentication, object.getRequest());
    }
}

