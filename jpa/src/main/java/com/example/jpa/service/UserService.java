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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        icesiUser.setUserId(UUID.randomUUID()); //User ID is generated by the service
        icesiUser.setIcesiAccountList(new ArrayList<>());

        if (icesiUser.getRole() != null && roleRepository.getByName(icesiUser.getRole().getName()).isPresent()) {
            IcesiRole icesiRole = roleRepository.getByName(icesiUser.getRole().getName()).get();
            icesiRole.getIcesiUserList().add(icesiUser);
            icesiUser.setRole(icesiRole);
            userRepository.save(icesiUser);
            roleRepository.save(icesiRole);
            return userMapper.fromUserToUserResponseDTO(icesiUser);
        }else {
            throw new NotFoundRoleException("Role " + icesiUser.getRole().getName() + " not found or is null");
        }
    }

    public List<UserResponseDTO> getUsers(){
        return userRepository.findAll().stream().map(userMapper::fromUserToUserResponseDTO).collect(Collectors.toList());
    }

}
