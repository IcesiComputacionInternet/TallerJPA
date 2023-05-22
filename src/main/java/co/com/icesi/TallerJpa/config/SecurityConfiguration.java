package co.com.icesi.TallerJpa.config;

import co.com.icesi.TallerJpa.controller.api.IcesiAccountApi;
import co.com.icesi.TallerJpa.controller.api.IcesiRoleApi;
import co.com.icesi.TallerJpa.controller.api.IcesiUserApi;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final IcesiAuthenticatorManager icesiAuthenticatorManager;
    private final String secret = "longenoughsecrettotestjwtencrypt";

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(icesiAuthenticatorManager);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> access) throws Exception{
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().access(access))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        byte[] bytes = secret.getBytes();
        SecretKeySpec key = new SecretKeySpec(bytes,0,bytes.length,"RSA");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> requestMatcherAuthorizationManager
            (HandlerMappingIntrospector introspector) {
        MvcRequestMatcher tempMvcRequestMatcher;
        RequestMatcher permitAll = new AndRequestMatcher(new MvcRequestMatcher(introspector, "/token"));
        RequestMatcherDelegatingAuthorizationManager.Builder managerBuilder
                = RequestMatcherDelegatingAuthorizationManager.builder()
                .add(permitAll, (context, other) -> new AuthorizationDecision(true));

        //ASIGNANDO LOS PERMISOS DEL API DE ROLES.
        //solo admin users puede añadir roles.
        tempMvcRequestMatcher = new MvcRequestMatcher(introspector, IcesiRoleApi.ROLE_BASE_URL);
        tempMvcRequestMatcher.setMethod(HttpMethod.POST);
        managerBuilder.add(tempMvcRequestMatcher,
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));
        //admin y bank users pueden consultar los roles.
        tempMvcRequestMatcher = new MvcRequestMatcher(introspector, IcesiRoleApi.ROLE_BASE_URL+"/**");
        tempMvcRequestMatcher.setMethod(HttpMethod.GET);
        managerBuilder.add(tempMvcRequestMatcher,
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN","SCOPE_BANK"));

        //ASIGNANDO LOS PERMISOS DEL API DE USUARIOS.
        //admin users pueden añadir usuarios.
        tempMvcRequestMatcher = new MvcRequestMatcher(introspector, IcesiUserApi.USER_BASE_URL);
        tempMvcRequestMatcher.setMethod(HttpMethod.POST);
        managerBuilder.add(tempMvcRequestMatcher,
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));
        //admin users pueden modificar rol de usuarios.
        tempMvcRequestMatcher = new MvcRequestMatcher(introspector, IcesiUserApi.USER_BASE_URL);
        tempMvcRequestMatcher.setMethod(HttpMethod.PATCH);
        managerBuilder.add(tempMvcRequestMatcher,
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));


        managerBuilder.add(new MvcRequestMatcher(introspector, IcesiAccountApi.ACCOUNT_BASE_URL+"/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_USER"));
        managerBuilder.add(new MvcRequestMatcher(introspector, IcesiAccountApi.ACCOUNT_BASE_URL),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_BANK"));

        managerBuilder.add(new MvcRequestMatcher(introspector, IcesiUserApi.USER_BASE_URL+"/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_USER","SCOPE_BANK"));


        AuthorizationManager<HttpServletRequest> manager = managerBuilder.build();
        return (authentication, object) -> manager.check(authentication, object.getRequest());
    }
}


