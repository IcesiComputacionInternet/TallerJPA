package com.example.tallerjpa.service;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.mapper.UserMapper;
import com.example.tallerjpa.model.IcesiUser;
import com.example.tallerjpa.repository.RoleRepository;
import com.example.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public IcesiUser saveIcesiUser(UserDTO userDTO){
        IcesiUser icesiUser = createIcesiUser(userDTO);
        return userRepository.save(icesiUser);
    }

    public IcesiUser createIcesiUser(UserDTO userDTO){
        IcesiUser icesiUser = userMapper.fromUserDTO(userDTO);
        boolean existsByEmail = userRepository.existsByEmail(userDTO.getEmail());
        boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(userDTO.getPhoneNumber());
        if (existsByEmail&&existsByPhoneNumber){
            throw new RuntimeException("The email and the phone number already exists");
        }
        if (existsByEmail){
            throw new RuntimeException("This email already exists");
        }
        if (existsByPhoneNumber){
            throw new RuntimeException("This phone number already exists");
        }
        icesiUser.setUserId(UUID.randomUUID());
        if(userDTO.getRole().isEmpty()){
            throw new RuntimeException("The role can't be null");
        }
        icesiUser.setIcesiRole(roleRepository.searchByName(userDTO.getRole()).orElseThrow(()-> new RuntimeException("Role doesn't exists")));
        return icesiUser;

    }



}
