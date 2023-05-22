package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.JwtResponse;
import co.com.icesi.TallerJPA.security.CustomAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenService {
    private final JwtEncoder encoder;
    public JwtResponse generateToken(Authentication authentication){
        CustomAuthentication customAuthentication = (CustomAuthentication) authentication;
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope",scope)
                .claim("icesiUserId",customAuthentication.getUserId())
                .build();
        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claimsSet);

        return JwtResponse.builder().token(this.encoder.encode(encoderParameters).getTokenValue()).build();

    }
}
