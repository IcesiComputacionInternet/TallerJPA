package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.model.SecurityUser;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {
    private final IcesiUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(SecurityUser::new).orElseThrow(()-> new UsernameNotFoundException("username not found: "+username));
    }
}
