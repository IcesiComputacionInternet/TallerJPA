package co.com.icesi.tallerjpa.model.security;

import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Stream;

public record SecurityUser(IcesiUser icesiUser) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(icesiUser).map(IcesiUser::getRole).map(Role::getName).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return icesiUser.getPassword();
    }

    @Override
    public String getUsername() {
        return icesiUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
