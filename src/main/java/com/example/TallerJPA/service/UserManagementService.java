package com.example.TallerJPA.service;

import com.example.TallerJPA.model.SecurityUser;
import com.example.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {
    private final UserRepository userManagementRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userManagementRepository.findByEmail(username).map(SecurityUser::new).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
