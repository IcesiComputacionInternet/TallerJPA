package com.edu.icesi.TallerJPA.unit;

import lombok.Getter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Getter
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    private final Jwt jwt;

    public CustomJwtAuthenticationToken(Jwt jwt) {
        super(jwt, null);
        this.jwt = jwt;
    }
}
