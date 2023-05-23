package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.model.SecurityUser;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {

    private final UserRespository userRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRespository.findIcesiUserByEmail(username).map(SecurityUser::new).orElseThrow(()->new UsernameNotFoundException("User not found: " + username));
    }
}
