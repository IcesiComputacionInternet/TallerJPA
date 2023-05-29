package com.edu.icesi.TallerJPA.unit.service;

import com.edu.icesi.TallerJPA.mapper.UserMapper;
import com.edu.icesi.TallerJPA.mapper.UserMapperImpl;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import com.edu.icesi.TallerJPA.service.UserService;
import com.edu.icesi.TallerJPA.unit.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class UserServiceTest {
/*
    private UserService userService;

    private UserRepository userRepository;

    private UserMapper userMapper;

    private RoleRepository roleRepository;

    @BeforeEach
    private void init() {
        userRepository = mock(UserRepository.class);
        userMapper = spy(UserMapperImpl.class);
        roleRepository = mock(RoleRepository.class);
        userService = new UserService(userRepository, userMapper, roleRepository);
    }

    @Test
    public void testCreateUser() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(createRole()));

        IcesiUserDTO icesiUser = userService.save(createDefaultDTO());
        IcesiUser icesiUser1 = IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .password("1234")
                .icesiRole(createRole())
                .build();

        verify(userRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser1)));
    }

    @Test
    public void testIcesiUserWithEmailAlreadyExists() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(createRole()));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createIcesiUser()));
        IcesiUserDTO user = createDefaultDTO();

        try {
            userService.save(user);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("User with email "+user.getEmail()+" already exists", messageOfException);
        }
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void testIcesiUserWithPhoneNumberAlreadyExists() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(createRole()));
        when(userRepository.findByPhoneNumber("1234567")).thenReturn(Optional.of(createIcesiUser()));
        IcesiUserDTO user = createDefaultDTO();

        try {
            userService.save(user);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("User with phone number "+user.getPhoneNumber()+" already exists", messageOfException);
        }
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void testIcesiUserWithPhoneNumberAndEmailAlreadyExists() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(createRole()));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createIcesiUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createIcesiUser()));

        try {
            userService.save(createDefaultDTO());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("User with this email and phone number already exists", messageOfException);
        }

        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void testIcesiUserWithNullRole() {
        try {
            userService.save(createIcesiUserDTOWithoutRole());

        } catch (RuntimeException exception) {

            String messageOfException = exception.getMessage();
            assertEquals("Role can't be null", messageOfException);
        }
        verify(userRepository, times(0)).save(any());
    }

    private IcesiUserDTO createDefaultDTO() {
        return IcesiUserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .password("1234")
                .icesiRole(createRole())
                .build();
    }

    private IcesiUserDTO createIcesiUserDTOWithoutRole() {
        return IcesiUserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("123456")
                .password("1234")
                .build();
    }

    private IcesiUser createIcesiUser() {
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("123456")
                .password("1234")
                .icesiRole(createRole())
                .build();
    }

    private IcesiRole createRole() {
        return IcesiRole.builder()
                .name("Student")
                .description("Is a student at Icesi")
                .build();
    }

 */

}