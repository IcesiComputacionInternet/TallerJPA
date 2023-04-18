package com.example.TallerJPA.service;

import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.mapper.UserMapper;
import com.example.TallerJPA.model.IcesiRole;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.repository.RoleRepository;
import com.example.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    public IcesiUser save(UserCreateDTO user) {
        Optional<IcesiUser> userFoundByEmail = userRepository.findByEmail(user.getEmail());
        Optional<IcesiUser> userFoundByPhoneNumber = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if(userFoundByEmail.isPresent() && userFoundByPhoneNumber.isPresent()){
            throw new RuntimeException("User already exists with email: " + user.getEmail() + " and phone number: " + user.getPhoneNumber());
        }
        if(userFoundByEmail.isPresent()){
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }
        if(userFoundByPhoneNumber.isPresent()){
            throw new RuntimeException("User already exists with phone number: " + user.getPhoneNumber());
        }
        Optional<IcesiRole> roleFound = roleRepository.findByName(user.getRoleName());
        if(roleFound.isEmpty()){
            throw new RuntimeException("Role not found");
        }
        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(roleFound.get());
        return userRepository.save(icesiUser);
    }
}
