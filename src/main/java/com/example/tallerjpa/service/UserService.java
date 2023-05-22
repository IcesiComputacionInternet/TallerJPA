package com.example.tallerjpa.service;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.error.exception.CustomException;
import com.example.tallerjpa.mapper.UserMapper;
import com.example.tallerjpa.model.IcesiUser;
import com.example.tallerjpa.repository.RoleRepository;
import com.example.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public IcesiUser saveIcesiUser(UserDTO userDTO){
        IcesiUser icesiUser = createIcesiUser(userDTO);
        return userRepository.save(icesiUser);
    }

    public IcesiUser createIcesiUser(UserDTO userDTO){
        IcesiUser icesiUser = userMapper.fromUserDTO(userDTO);
        icesiUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        boolean existsByEmail = userRepository.existsByEmail(userDTO.getEmail());
        boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(userDTO.getPhoneNumber());
        if (existsByEmail&&existsByPhoneNumber){
            throw new CustomException("The email and the phone number already exists");
        }
        if (existsByEmail){
            throw new CustomException("This email already exists");
        }
        if (existsByPhoneNumber){
            throw new CustomException("This phone number already exists");
        }
        icesiUser.setUserId(UUID.randomUUID());
        if(userDTO.getRole()==null){
            throw new CustomException("The role can't be null");
        }
        icesiUser.setIcesiRole(roleRepository.searchByName(userDTO.getRole().getName().toUpperCase()).orElseThrow(()-> new CustomException("Role doesn't exists")));
        return icesiUser;

    }



}
