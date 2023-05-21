package com.example.demo.service;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;
import com.example.demo.mapper.IcesiUserMapper;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import com.example.demo.repository.IcesiRoleRepository;
import com.example.demo.repository.IcesiUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor 
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;
    private final IcesiRoleRepository icesiRoleRepository;

    public  ResponseIcesiUserDTO create(IcesiUserCreateDTO user) {

        Optional<IcesiUser> userWithEmail = icesiUserRepository.findByEmail(user.getEmail());
        Optional<IcesiUser> userWithPhone = icesiUserRepository.findByPhoneNumber(user.getPhoneNumber());
        
        if (userWithEmail.isPresent() && userWithPhone.isPresent()) {
            throw new RuntimeException("These email and phone number are already in use");
        }

        userWithEmail.ifPresent(u -> {throw new RuntimeException("This email is already in use");});
        userWithPhone.ifPresent(u -> {throw new RuntimeException("This phone number is already in use");});
        
        IcesiRole icesiRole = icesiRoleRepository.findByName(user.getIcesiRoleCreateDTO().getName())
            .orElseThrow(() -> new RuntimeException("This role is not present in the database"));
        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserCreateDTO(user);

        icesiUser.setIcesiRole(icesiRole);
        icesiUser.setUserId(UUID.randomUUID());

        return icesiUserMapper.fromIcesiUserToResponseIcesiUserDTO(icesiUserRepository.save(icesiUser));

    }

}
