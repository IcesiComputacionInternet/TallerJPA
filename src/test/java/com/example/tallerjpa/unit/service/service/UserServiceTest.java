package com.example.tallerjpa.unit.service.service;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.mapper.UserMapper;
import com.example.tallerjpa.mapper.UserMapperImpl;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.model.IcesiUser;
import com.example.tallerjpa.repository.RoleRepository;
import com.example.tallerjpa.repository.UserRepository;
import com.example.tallerjpa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private RoleRepository roleRepository;


    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userMapper = spy(UserMapperImpl.class);
        userService = new UserService(userRepository, roleRepository, userMapper);
    }


    private UserDTO defaultUserDTO(){
        return UserDTO.builder()
                .firstName("Juan")
                .lastName("Osorio")
                .email("juanosorio@hotmail.com")
                .phoneNumber("12345678")
                .password("password")
                .role("Student")
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("Student")
                .description("Student of the Icesi University")
                .build();
    }
}
