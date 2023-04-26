package com.example.tallerjpa.service;

import com.example.tallerjpa.model.SecurityUser;
import com.example.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class UserManagementService implements UserDetailsService{

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return repository.searchByEmail(username).map(SecurityUser::new).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
