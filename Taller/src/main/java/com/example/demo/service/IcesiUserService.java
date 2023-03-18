package com.example.demo.service;

import com.example.demo.Repository.IcesiUserRepository;
import com.example.demo.model.IcesiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IcesiUserService {
    @Autowired
    private IcesiUserRepository userRepository;

    public IcesiUser createUser(IcesiUser user) throws DuplicateKeyException {

        //Role validation
        if (user.getIcesiRole() == null) {
            throw new IllegalArgumentException("user role can't be null");
        }

        //Generation of UUID
        user.setUserId(UUID.randomUUID());

        IcesiUser existingUser = userRepository.findByEmail(user.getEmail());
        IcesiUser existingUserByPhone = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if (existingUser != null && existingUserByPhone != null) {
            throw new DuplicateKeyException("Email and phone number already exists");
        }

        if (existingUser != null) {
            throw new DuplicateKeyException("Email already exists");
        }

        if (existingUserByPhone != null) {
            throw new DuplicateKeyException("Phone number already exists");
        }

        return userRepository.save(user);
    }
}
