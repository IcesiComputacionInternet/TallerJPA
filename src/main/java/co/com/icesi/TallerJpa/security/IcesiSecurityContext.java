package co.com.icesi.TallerJpa.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class IcesiSecurityContext {

    public static String getCurrentUserId(){
        return ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getToken().getClaimAsString("icesiUserId");
    }

    public static String getCurrentUserRole(){
        return((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getToken().getClaimAsString("scope");
    }
}
