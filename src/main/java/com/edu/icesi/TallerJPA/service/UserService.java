package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.mapper.UserMapper;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    public UserCreateDTO save(UserCreateDTO userCreateDTO){

        if(userRepository.findByEmail(userCreateDTO.getEmail()).isPresent() && userRepository.findByPhoneNumber(userCreateDTO.getPhoneNumber()).isPresent()){
            throw new RuntimeException("User with this email and phone number already exists");
        }

        findByEmail(userCreateDTO.getEmail());
        findByPhoneNumber(userCreateDTO.getPhoneNumber());
        validationRole(userCreateDTO.getIcesiRole());

        String roleName = userCreateDTO.getIcesiRole().getName();
        roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("The role "+roleName+" not exists"));

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(userCreateDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setIcesiRole(userCreateDTO.getIcesiRole());

        return userMapper.fromIcesiUser(userRepository.save(icesiUser));
    }

    public UserCreateDTO findByEmail(String email){
        return userMapper.fromIcesiUser(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with this email already exists")));
    }

    public UserCreateDTO findByPhoneNumber(String phoneNumber){
        return userMapper.fromIcesiUser(userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User with this phone number already exists")));
    }

    public void validationRole(IcesiRole role){
        if (role == null){
            throw new RuntimeException("Role can't be null");
        }
    }
}
