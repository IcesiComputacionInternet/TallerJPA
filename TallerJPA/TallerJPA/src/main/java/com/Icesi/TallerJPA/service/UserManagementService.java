package com.Icesi.TallerJPA.service;

import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.model.SecurityUser;
import com.Icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {
    private final IcesiUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(SecurityUser :: new).orElseThrow(()-> new UsernameNotFoundException("Username not found"+ username));
    }


}
