package com.example.jpa.service;

import com.example.jpa.dto.UserRequestDTO;
import com.example.jpa.dto.UserResponseDTO;
import com.example.jpa.exceptions.NotFoundRoleException;
import com.example.jpa.mapper.UserMapper;
import com.example.jpa.model.IcesiRole;
import com.example.jpa.model.IcesiUser;
import com.example.jpa.repository.RoleRepository;
import com.example.jpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserResponseDTO save(UserRequestDTO user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()
                && userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Already exists an user with the same email and phone number");
        }else if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Already exists an user with the same email");
        }else if(userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Already exists an user with the same phone number");
        }
        IcesiUser icesiUser = userMapper.fromUserRequestDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setIcesiRole(roleRepository.findByName(user.getRole()).orElseThrow(() -> new NotFoundRoleException("Role name not found")));
        return userMapper.fromUserToUserResponseDTO(icesiUser);
    }

}
