package com.example.TallerJPA.service;

import com.example.TallerJPA.dto.UserDTO;
import com.example.TallerJPA.mapper.UserMapper;
import com.example.TallerJPA.model.IcesiRole;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.repository.RoleRepository;
import com.example.TallerJPA.repository.UserRepository;
import com.example.TallerJPA.security.IcesiSecurityContext;
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
    public UserDTO save(UserDTO user) {
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
        if(user.getRoleName().equals("ADMIN") && !checkAdminPermissions()){
            throw new RuntimeException("You don't have permissions to create an admin user");
        }
        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(roleFound.get());
        return userMapper.fromIcesiUser(userRepository.save(icesiUser));
    }

    private boolean checkAdminPermissions(){
        Optional<IcesiUser> userFound = userRepository.findById(UUID.fromString(IcesiSecurityContext.getCurrentUserId()));
        IcesiUser user = userFound.orElseThrow(RuntimeException::new);
        if(user.getRole().getName().equals("ADMIN")){
            return true;
        }
        return false;
    }
}
