package com.example.TallerJPA.service;

import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.mapper.UserMapper;
import com.example.TallerJPA.model.IcesiRole;
import com.example.TallerJPA.model.IcesiUser;
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
    private final RoleService roleService;
    public IcesiUser save(UserCreateDTO user) {
        Optional<IcesiUser> userFoundByEmail = userRepository.findByEmail(user.getEmail());
        Optional<IcesiUser> userFoundByPhoneNumber = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if(userFoundByEmail.isPresent() || userFoundByPhoneNumber.isPresent() || user.getRole() == null){
            if(userFoundByEmail.isPresent()){
                throw new RuntimeException("User already exists with email: " + user.getEmail());
            }else if(userFoundByPhoneNumber.isPresent()){
                throw new RuntimeException("User already exists with phone number: " + user.getPhoneNumber());
            }else{
                throw new RuntimeException("User role is null");
            }
        }else{
            Optional<IcesiRole> roleFound = roleService.findRoleByName(user.getRole());
            if(roleFound.isEmpty()){
                throw new RuntimeException("Role not found");
            }
            IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
            icesiUser.setUserId(UUID.randomUUID());
            icesiUser.setRole(roleFound.get());
            int i = 0;
            return userRepository.save(icesiUser);
        }

    }
    public Optional<IcesiUser> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<IcesiUser> findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}
