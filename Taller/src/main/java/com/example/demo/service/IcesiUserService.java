package com.example.demo.service;

import com.example.demo.DTO.IcesiUserDTO;
import com.example.demo.Mapper.IcesiUserMapper;
import com.example.demo.repository.IcesiUserRepository;
import com.example.demo.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class IcesiUserService {
    @Autowired
    private final IcesiUserRepository userRepository;
    private final IcesiUserMapper userMapper;

    public IcesiUser createUser(IcesiUserDTO user) {

        //Role validation

        if (user.getIcesiRoleId() == null) {
            throw new IllegalArgumentException("user role can't be null");
        }

        //Generation of UUID
        user.setUserId(UUID.randomUUID());

        Optional<IcesiUser> existingUser = userRepository.findByEmail(user.getEmail());
        Optional<IcesiUser> existingUserByPhone = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if (existingUser.isPresent() && existingUserByPhone.isPresent()) {
            throw new DuplicateKeyException("Email and phone number already exists");
        }

        if (existingUser.isPresent()) {
            throw new DuplicateKeyException("Email already exists");
        }

        if (existingUserByPhone.isPresent()) {
            throw new DuplicateKeyException("Phone number already exists");
        }

        return userRepository.save(userMapper.fromIcesiUserDTO(user));
    }

    public Optional<IcesiUser> findById(UUID fromString) {
        return null;
    }
}
