package co.edu.icesi.tallerjpa.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class IcesiSecurityContext {

    public static String getCurrentUserId(){
        return ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getToken().getClaimAsString("icesiUserId");
    }
}
