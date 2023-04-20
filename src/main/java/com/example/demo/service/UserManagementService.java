package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.SecurityUser;
import com.example.demo.repository.IcesiUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserManagementService  implements UserDetailsService {
    
    private final IcesiUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("username not found " + username));
    }
}
