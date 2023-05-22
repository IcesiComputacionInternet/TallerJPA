package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.model.SecurityUser;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserManagerService implements UserDetailsService {
    private  final IcesiUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("username not found: "+username));
    }
}
