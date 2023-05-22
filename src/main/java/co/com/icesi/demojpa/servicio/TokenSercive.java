package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.LoginDTO;
import co.com.icesi.demojpa.dto.TokenDTO;
import co.com.icesi.demojpa.security.CustomAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenSercive {

    private final JwtEncoder encoder;

    private final AuthenticationManager authenticationManager;

    public TokenDTO generateToken(Authentication authentication){
        CustomAuthentication customAuthentication =(CustomAuthentication) authentication;
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("icesiUserId", customAuthentication.getUserId())
                .build();
        var encoderParameters= JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims);
        return TokenDTO.builder().token(encoder.encode(encoderParameters).getTokenValue()).build();

    }

    public TokenDTO logIn(@RequestBody LoginDTO loginDTO){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
            return generateToken(authentication);
        }catch (Exception e) {
            //TODO no se si deberia ir como exception porque afecta el funcionamiento del front
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        //return null;
    }
}
