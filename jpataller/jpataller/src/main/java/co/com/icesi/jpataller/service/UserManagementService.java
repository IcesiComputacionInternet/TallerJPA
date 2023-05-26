package co.com.icesi.jpataller.service;

import co.com.icesi.jpataller.model.SecurityUser;
import co.com.icesi.jpataller.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserManagementService implements UserDetailsService {

    private final IcesiUserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(SecurityUser::new).orElseThrow(()-> new UsernameNotFoundException("user not foundc"+username));
    }
}