package com.example.tallerjpa.unit.service.service;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.mapper.UserMapper;
import com.example.tallerjpa.mapper.UserMapperImpl;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.model.IcesiUser;
import com.example.tallerjpa.repository.RoleRepository;
import com.example.tallerjpa.repository.UserRepository;
import com.example.tallerjpa.service.UserService;
import com.example.tallerjpa.unit.service.matcher.UserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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

    @Test
    public void testSaveUser() {
        when(roleRepository.searchByName(any())).thenReturn(Optional.of(defaultRole()));
        userService.saveIcesiUser(defaultUserDTO());
        verify(userRepository, times(1)).save(argThat(new UserMatcher(defaultUser())));
        verify(userMapper, times(1)).fromUserDTO(defaultUserDTO());
    }

    @Test
    public void testSaveUserRoleDoesNotExists(){
        when(roleRepository.existsByName(any())).thenReturn(true);
        try {
            userService.saveIcesiUser(defaultUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Role doesn't exists", message);
        }
    }

    @Test
    public void testSaveUserEmailExists() {
        when(userRepository.existsByEmail(any())).thenReturn(true);
        try {
            userService.saveIcesiUser(defaultUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("This email already exists", message);
        }
    }

    @Test
    public void testSaveUserPhoneNumberExists() {
        when(userRepository.existsByPhoneNumber(any())).thenReturn(true);
        try {
            userService.saveIcesiUser(defaultUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("This phone number already exists", message);
        }
    }

    @Test
    public void testSaveUserEmailAndPhoneNumberExists() {
        when(userRepository.existsByEmail(any())).thenReturn(true);
        when(userRepository.existsByPhoneNumber(any())).thenReturn(true);
        try {
            userService.saveIcesiUser(defaultUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("The email and the phone number already exists", message);
        }
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

    private IcesiUser defaultUser(){
        return IcesiUser.builder()
                .firstName("Juan")
                .lastName("Osorio")
                .email("juanosorio@hotmail.com")
                .phoneNumber("12345678")
                .password("password")
                .icesiRole(defaultRole())
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
