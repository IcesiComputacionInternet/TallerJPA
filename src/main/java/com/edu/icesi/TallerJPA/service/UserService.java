package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.mapper.UserMapper;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public IcesiUser save(UserCreateDTO userCreateDTO){

        if(userRepository.findByEmail(userCreateDTO.getEmail()).isPresent() && userRepository.findByPhoneNumber(userCreateDTO.getPhoneNumber()).isPresent()){
            throw new RuntimeException("User with this email and phone number already exists");

        } else if(userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()){
            throw new RuntimeException("User with this email already exists");

        }else if(userRepository.findByPhoneNumber(userCreateDTO.getPhoneNumber()).isPresent()){
            throw new RuntimeException("User with this phone number already exists");

        }else if(userCreateDTO.getIcesiRole() == null){
            throw new RuntimeException("Role can't be null");
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(userCreateDTO);
        icesiUser.setUserId(UUID.randomUUID());

        return userRepository.save(icesiUser);
    }
}
