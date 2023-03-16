package com.example.TallerJPA.service;

import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.mapper.UserMapper;
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
    public IcesiUser save(UserCreateDTO user) {
        Optional<IcesiUser> userFoundByEmail = userRepository.findByEmail(user.getEmail());
        Optional<IcesiUser> userFoundByPhoneNumber = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if(userFoundByEmail.isPresent() || userFoundByPhoneNumber.isPresent()){
            throw new RuntimeException("User already exists");
        }else{
            IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
            icesiUser.setUserId(UUID.randomUUID());
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
