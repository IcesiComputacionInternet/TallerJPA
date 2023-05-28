package co.com.icesi.TallerJPA.config;

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
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    public final String secret = "longenoughsecrettotestjwtencrypt";

    private final IcesiAuthenticationManager icesiAuthenticationManager;

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(icesiAuthenticationManager);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> acces) throws Exception {
        return http.cors()
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().access(acces))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        byte[] bytes = secret.getBytes();
        SecretKeySpec key = new SecretKeySpec(bytes,0, bytes.length,"RSA");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> requestAuthorizationContextAuthorizationManager(HandlerMappingIntrospector introspector){
        RequestMatcher permitAll = new AndRequestMatcher(new MvcRequestMatcher(introspector,"/auth/login"));
      //  RequestMatcher permit2 = new AndRequestMatcher(new MvcRequestMatcher(introspector,"/account/getAccounts"));

        //TODO: Probar endpoint desde el back
        RequestMatcherDelegatingAuthorizationManager.Builder mangerBuilder
                = RequestMatcherDelegatingAuthorizationManager.builder().add(permitAll,(context,other) -> new AuthorizationDecision(true));
       // mangerBuilder.add(permit2,(context,other) -> new AuthorizationDecision(true));
        mangerBuilder.add(new MvcRequestMatcher(introspector,"/user"), AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_BANK", "SCOPE_ADMIN"));
        mangerBuilder.add(new MvcRequestMatcher(introspector,"/role"), AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_ADMIN"));
        mangerBuilder.add(new MvcRequestMatcher(introspector,"/account"), AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_NORMAL","SCOPE_BANK","SCOPE_ADMIN"));
        mangerBuilder.add(new MvcRequestMatcher(introspector,"/account/**"), AuthorityAuthorizationManager.hasAnyAuthority("SCOPE_NORMAL","SCOPE_ADMIN"));




        AuthorizationManager<HttpServletRequest> manager = mangerBuilder.build();
        return ((authentication, object) -> manager.check(authentication,object.getRequest()));
    }


}
