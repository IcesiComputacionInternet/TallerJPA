package co.edu.icesi.tallerjpa.segurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import javax.security.auth.Subject;
import java.util.Collection;

public class CustomAuthentication implements Authentication {

    private final Authentication delegateAuthentication;

    private final String userId;

    public CustomAuthentication(Authentication authentication, String userId) {
        this.delegateAuthentication = authentication;
        this.userId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegateAuthentication.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return delegateAuthentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return delegateAuthentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return delegateAuthentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return delegateAuthentication.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        delegateAuthentication.setAuthenticated(isAuthenticated);
    }

    @Override
    public String getName() {
        return delegateAuthentication.getName();
    }

    @Override
    public boolean implies(Subject subject) {
        return delegateAuthentication.implies(subject);
    }

    public String getUserId() {
        return userId;
    }
}
