package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.mapper.UserMapper;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserCreateDTO save(UserCreateDTO userCreateDTO){

        if(userRepository.findByEmail(userCreateDTO.getEmail()).isPresent() && userRepository.findByPhoneNumber(userCreateDTO.getPhoneNumber()).isPresent()){
            throw new RuntimeException("User with this email and phone number already exists");
        }

        findByEmail(userCreateDTO.getEmail());
        findByPhoneNumber(userCreateDTO.getPhoneNumber());
        validationRole(userCreateDTO.getIcesiRole());

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(userCreateDTO);
        icesiUser.setUserId(UUID.randomUUID());

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
