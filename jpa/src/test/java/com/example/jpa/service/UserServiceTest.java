package com.example.jpa.service;

import com.example.jpa.dto.RoleDTO;
import com.example.jpa.dto.UserRequestDTO;
import com.example.jpa.mapper.UserMapper;
import com.example.jpa.mapper.UserMapperImpl;
import com.example.jpa.matcher.UserMatcher;
import com.example.jpa.model.IcesiRole;
import com.example.jpa.model.IcesiUser;
import com.example.jpa.repository.RoleRepository;
import com.example.jpa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserService userService;
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        userMapper = spy(UserMapperImpl.class);
        roleRepository = mock(RoleRepository.class);
        userService = new UserService(userRepository, userMapper, roleRepository);
    }

    @Test
    public void testSaveUser() {
        when(roleRepository.getByName(any())).thenReturn(Optional.ofNullable(defaultRole()));
        userService.save(defaultUserDTO());
        verify(roleRepository, times(1)).getByName(any());
        verify(userRepository, times(1)).save(argThat(new UserMatcher(defaultUser())));
        verify(roleRepository, times(1)).save(any());
        verify(userMapper, times(1)).fromUserRequestDTO(any());
    }

    @Test
    public void saveUserWhenEmailAndPhoneAlreadyExist(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            userService.save(defaultUserDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("Already exists an user with the same email and phone number",exceptionMessage);
            verify(userRepository, times(0)).save(any());
        }
    }

    @Test
    public void saveUserWhenEmailAlreadyExist(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());
        try {
            userService.save(defaultUserDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("Already exists an user with the same email",exceptionMessage);
            verify(userRepository, times(0)).save(any());
        }
    }

    @Test
    public void saveUserWhenPhoneAlreadyExist(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            userService.save(defaultUserDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("Already exists an user with the same phone number",exceptionMessage);
            verify(userRepository, times(0)).save(any());
        }
    }

    @Test
    public void saveUserWhenRoleDoesNotExistOrIsNull(){
        when(roleRepository.getByName(any())).thenReturn(Optional.empty());
        try {
            userService.save(defaultUserDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("Role " + defaultUserDTO().getRole().getName() + " not found or is null",exceptionMessage);
            verify(userRepository, times(0)).save(any());
        }
    }

    private UserRequestDTO defaultUserDTO() {
        return UserRequestDTO.builder()
                .firstName("Juan")
                .lastName("Perez")
                .password("123456")
                .email("jdoe@gmail.com")
                .phoneNumber("1234567890")
                .role(defaultRoleDTO())
                .build();
    }

    private IcesiUser defaultUser() {
        return IcesiUser.builder()
                .firstName("Juan")
                .lastName("Perez")
                .password("123456")
                .email("jdoe@gmail.com")
                .phoneNumber("1234567890")
                .role(defaultRole())
                .build();
    }

    private RoleDTO defaultRoleDTO() {
        return RoleDTO.builder()
                .name("Colaborador")
                .description("User role for test")
                .build();
    }

    private IcesiRole defaultRole() {
        return IcesiRole.builder()
                .name("Colaborador")
                .description("User role for test")
                .icesiUserList(new ArrayList<>())
                .build();
    }
}
