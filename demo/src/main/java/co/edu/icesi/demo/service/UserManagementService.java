package co.edu.icesi.demo.service;

import co.edu.icesi.demo.model.SecurityUser;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import co.edu.icesi.demo.repository.UserManagementRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {

    /*
    private final IcesiUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(SecurityUser::new).orElseThrow(()-> new UsernameNotFoundException("user not found"+ username));
    }
    */
    private final UserManagementRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("username not found: " + username));
    }
}