package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.model.SecurityUser;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {

    private final IcesiUserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("username not found " + username));
    }
}
