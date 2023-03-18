package com.edu.icesi.TallerJPA.unit.service;

import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.mapper.UserMapper;
import com.edu.icesi.TallerJPA.mapper.UserMapperImpl;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
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

    private UserService userService;

    private UserRepository userRepository;

    private UserMapper userMapper;

    @BeforeEach
    private void init() {
        userRepository = mock(UserRepository.class);
        userMapper = spy(UserMapperImpl.class);
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    public void testCreateUser() {
        IcesiUser icesiUser = userService.save(createDefaultDTO());
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
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createIcesiUser()));

        try {
            userService.save(createDefaultDTO());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("User with this email already exists", messageOfException);
        }
    }

    @Test
    public void testIcesiUserWithPhoneNumberAlreadyExists() {
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createIcesiUser()));

        try {
            userService.save(createDefaultDTO1());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("User with this phone number already exists", messageOfException);
        }
    }

    @Test
    public void testIcesiUserWithPhoneNumberAndEmailAlreadyExists() {
        userService.save(createDefaultDTO1());
        try {
            userService.save(createDefaultDTO());

        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("User with this email and phone number already exists", messageOfException);
        }
    }

    @Test
    public void testIcesiUserWithNullRole() {
        try {
            userService.save(createIcesiUserDTOWithoutRole());

        } catch (RuntimeException exception) {

            String messageOfException = exception.getMessage();
            assertEquals("Role can't be null", messageOfException);
        }
    }

    private UserCreateDTO createDefaultDTO() {
        return UserCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .password("1234")
                .icesiRole(createRole())
                .build();
    }

    private UserCreateDTO createDefaultDTO1() {
        return UserCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .password("1234")
                .icesiRole(createRole())
                .build();
    }

    private UserCreateDTO createIcesiUserDTOWithoutRole() {
        return UserCreateDTO.builder()
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
}
