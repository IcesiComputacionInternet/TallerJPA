package co.edu.icesi.tallerjpa.config;

import co.edu.icesi.tallerjpa.api.IcesiAccountApi;
import co.edu.icesi.tallerjpa.api.IcesiRoleApi;
import co.edu.icesi.tallerjpa.api.IcesiUserApi;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private final IcesiAuthenticatorManager icesiAuthenticatorManager;
    private final String secret = "longenoughsecrettotestjwtencrypt";
    @Bean
    public AuthenticationManager authenticationManagerBean(){
        return new ProviderManager(icesiAuthenticatorManager);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthorizationManager<RequestAuthorizationContext> access)
            throws Exception{
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().access(access)) //permitAll para que funcione | access(access)
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public JwtDecoder jwtEncoder(){
        byte[] bytes = secret.getBytes();
        SecretKeySpec key = new SecretKeySpec(bytes,0,bytes.length, "RSA");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }
    @Bean
    public JwtEncoder jwtDecoder(){return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));}

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> requestMatcherAuthorizationManager
            (HandlerMappingIntrospector introspector){
        MvcRequestMatcher tempMvcRequestMatcher;
        RequestMatcher permitAll = new AndRequestMatcher(new MvcRequestMatcher(introspector,"/token"));
        RequestMatcherDelegatingAuthorizationManager.Builder managerBuilder =
                RequestMatcherDelegatingAuthorizationManager.builder()
                        .add(permitAll,(context,other)->new AuthorizationDecision(true));

        managerBuilder.add(new MvcRequestMatcher(introspector, IcesiAccountApi.ROOT_PATH+"/**"),
                AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"));

        tempMvcRequestMatcher = new MvcRequestMatcher(introspector, IcesiRoleApi.ROOT_PATH);
        tempMvcRequestMatcher.setMethod(HttpMethod.POST);
        managerBuilder.add(tempMvcRequestMatcher, AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));

        tempMvcRequestMatcher = new MvcRequestMatcher(introspector, IcesiUserApi.ROOT_PATH);
        tempMvcRequestMatcher.setMethod(HttpMethod.POST);
        managerBuilder.add(tempMvcRequestMatcher, AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN", "SCOPE_BANK"));

        tempMvcRequestMatcher = new MvcRequestMatcher(introspector, IcesiUserApi.ROOT_PATH+"/{icesiUserId}/role/{roleName}");
        tempMvcRequestMatcher.setMethod(HttpMethod.PATCH);
        managerBuilder.add(tempMvcRequestMatcher, AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));

        AuthorizationManager<HttpServletRequest> manager = managerBuilder.build();
        return (authentication, object) -> manager.check(authentication,object.getRequest());
    }
}
