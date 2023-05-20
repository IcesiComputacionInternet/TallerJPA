package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.config.PasswordEncoderConfiguration;
import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import com.icesi.TallerJPA.dto.response.IcesiUserResponseDTO;
import com.icesi.TallerJPA.error.util.IcesiExceptionBuilder;
import com.icesi.TallerJPA.mapper.UserMapper;
import com.icesi.TallerJPA.model.IcesiUser;
import com.icesi.TallerJPA.repository.RoleRepository;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final UserRespository userRespository;

    private final RoleRepository roleRepository;

    private final IcesiExceptionBuilder eb = new IcesiExceptionBuilder();

    private final PasswordEncoderConfiguration encoder = new PasswordEncoderConfiguration();


    public IcesiUserResponseDTO save(IcesiUserDTO user) {

        Boolean existEmail = userRespository.existsByEmail(user.getEmail());
        Boolean existPhone = userRespository.existsByPhoneNumber(user.getPhoneNumber());

        if(existEmail && existPhone){
            throw eb.exceptionDuplicate("Email and phone are repeated", "user", "email and phone", user.getEmail() + " y " + user.getPhoneNumber());}
        if(existEmail){
            throw eb.exceptionDuplicate("Email is repeated", "user", "email", user.getEmail());}
        if(existPhone){
            throw eb.exceptionDuplicate("Phone is repeated", "user", "phone", user.getPhoneNumber());}

        return createUser(user);
    }

    public IcesiUserResponseDTO createUser(IcesiUserDTO user) {
        IcesiUser icesiUser = userMapper.fromIcesiUser(user);
        icesiUser.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
        icesiUser.setUserId(UUID.randomUUID());
        if(user.getRolName().equals("ADMIN") && roleBank()){
            throw eb.exceptionUnauthorized("You don't have permission to create an admin user", "user" );
        }

        icesiUser.setIcesiRole(roleRepository.findIcesiRoleByName(user.getRolName()).orElseThrow(() -> {
            throw eb.exceptionNotFound("Role not found", user.getRolName());
        }));
        return userMapper.toResponse(userRespository.save(icesiUser));
    }

    public Boolean roleBank(){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRespository.findIcesiUserByEmail(username).orElseThrow(()->{
            throw eb.exceptionNotFound("User not found", username);
        });
        return user.getIcesiRole().getName().equals("BANK");
    }
}
