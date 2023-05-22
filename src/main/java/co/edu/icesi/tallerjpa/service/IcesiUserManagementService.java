package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.model.IcesiSecurityUser;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class IcesiUserManagementService implements UserDetailsService {

    private final IcesiUserRepository userRepository;

    public IcesiUserManagementService(IcesiUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findIcesiUserByEmail(username)
                .map(IcesiSecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
