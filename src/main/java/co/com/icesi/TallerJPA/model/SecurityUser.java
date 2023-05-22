package co.com.icesi.TallerJPA.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class SecurityUser implements UserDetails {
    private final IcesiUser icesiUser;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(icesiUser).map(IcesiUser::getRole).map(IcesiRole::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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

    public IcesiUser getIcesiUser(){
        return icesiUser;
    }


}
