package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.model.SecurityUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {

    private final IcesiUserRepository icesiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return icesiUserRepository.findByEmail(username).map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
