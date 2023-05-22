package com.Icesi.TallerJPA.service;

import com.Icesi.TallerJPA.dto.TokenDTO;
import com.Icesi.TallerJPA.security.CustonAuthentication;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TokenService {
    private final JwtEncoder encoder;

    public TokenDTO generateToken(Authentication authentication) {
        CustonAuthentication customAuthentication = (CustonAuthentication) authentication;
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("icesiUSerId", customAuthentication.getUserId())
                .build();
        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        //return this.encoder.encode(encoderParameters).getTokenValue();
        TokenDTO token=  new TokenDTO(this.encoder.encode(encoderParameters).getTokenValue());
        return token;
    }
}
