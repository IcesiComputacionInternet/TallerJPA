package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.dto.RequestUserDTO;
import com.edu.icesi.demojpa.mapper.UserMapper;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.RoleRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public IcesiUser save(RequestUserDTO user){
        boolean emailInUse = userRepository.isEmailInUse(user.getEmail()).isPresent();
        boolean phoneNumberInUse = userRepository.isPhoneNumberInUse(user.getPhoneNumber()).isPresent();
        IcesiRole role = roleRepository.findRoleByName(user.getRoleType()).orElseThrow(() -> new RuntimeException("The role "+ user.getRoleType() +" doesn't exist"));

        List<RuntimeException> errors = new ArrayList<>();

        if(emailInUse){
            errors.add(new RuntimeException("The email is already in use"));
        }

        if(phoneNumberInUse){
            errors.add(new RuntimeException("The phone-number is already in use"));
        }

        if (!errors.isEmpty()){
            errors.stream().map(RuntimeException::getMessage).forEach(System.out::println);
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(role);
        return userRepository.save(icesiUser);
    }
}
