package icesi.university.accountSystem.services;

import icesi.university.accountSystem.model.SecurityUser;
import icesi.university.accountSystem.repository.IcesiUserRepository;
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        return repository.findByEmail(userName).map(SecurityUser::new).orElseThrow(()-> new UsernameNotFoundException("username not found: " + userName));
    }
}
