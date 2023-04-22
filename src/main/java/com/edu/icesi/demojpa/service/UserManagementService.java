package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.model.SecurityUser;
import com.edu.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.isEmailInUse(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
    }
}