package co.com.icesi.TallerJPA.service.security;

import co.com.icesi.TallerJPA.model.SecurityUser;
import co.com.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService  implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return repository.findUserByEmail(username).map(SecurityUser::new).orElseThrow(()->new UsernameNotFoundException("User not found: "+ username));

    }
}
