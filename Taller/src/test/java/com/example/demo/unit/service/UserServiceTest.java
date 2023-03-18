package com.example.demo.unit.service;

import com.example.demo.DTO.IcesiUserDTO;
import com.example.demo.Mapper.IcesiUserMapper;
import com.example.demo.Repository.IcesiUserRepository;
import com.example.demo.model.IcesiUser;
import com.example.demo.service.IcesiUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private IcesiUserRepository userRepository;

    @Mock
    private IcesiUserMapper userMapper;

    @InjectMocks
    private IcesiUserService userService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser_userIsCreatedSuccessfully() throws Exception {
        // Test case 1: user is created successfully
        IcesiUserDTO userDTO = IcesiUserDTO.builder()
                .email("test@example.com")
                .phoneNumber("123456789")
                .icesiRoleId(UUID.fromString("a74637b7-ca5d-4ee9-bacf-b3f946ffe4cb"))
                .build();
        UUID userId = UUID.randomUUID();
        IcesiUser user = IcesiUser.builder()
                .email("test@example.com")
                .phoneNumber("123456789")
                .userId(userId)
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.findByPhoneNumber("123456789")).thenReturn(null);
        when(userMapper.fromIcesiUserDTO(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userService.createUser(userDTO));
    }

    @Test
    void emailAlreadyExists() {
        // Test case 2: email already exists
        UUID userId = UUID.randomUUID();
        IcesiUserDTO userDTO = IcesiUserDTO.builder()
                .email("test@example.com")
                .phoneNumber("123456789")
                .icesiRoleId(UUID.fromString("a74637b7-ca5d-4ee9-bacf-b3f946ffe4cb"))
                .build();
        IcesiUser user = IcesiUser.builder()
                .email("test@example.com")
                .phoneNumber("123456789")
                .userId(userId)
                .build();
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        assertThrows(DuplicateKeyException.class, () -> userService.createUser(userDTO));

        verify(userRepository).findByEmail(eq("test@example.com"));
        verify(userRepository, never()).save(any(IcesiUser.class));
    }
}
