package com.Icesi.TallerJPA.unit.service;

import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.mapper.IcesiUserMapper;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.repository.IcesiRoleRepository;
import com.Icesi.TallerJPA.repository.IcesiUserRepository;
import com.Icesi.TallerJPA.service.IcesiUserService;
import com.Icesi.TallerJPA.unit.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.Icesi.TallerJPA.mapper.IcesiUserMapperImpl;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private IcesiUserService userService;

    private IcesiUserRepository icesiUserRepository;
    private IcesiRoleRepository icesiRoleRepository;
    private IcesiUserMapper userMapper;

    @BeforeEach
    private void init() {
        icesiUserRepository = mock(IcesiUserRepository.class);
        userMapper = spy(IcesiUserMapperImpl.class);

        userService = new IcesiUserService(icesiUserRepository, icesiRoleRepository,userMapper);
    }


    @Test
    public void testExistingEail() {
        when(icesiUserRepository.findEmail(any())).thenReturn(Optional.of(createIcesiUser()));

        try {
            userService.save(icesiUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("User with this email already exists", messageOfException);
        }
    }

    @Test
    public void testIcesiUserWithPhoneNumberAlreadyExists() {
        when(icesiUserRepository.findByPhoneNum(any())).thenReturn(Optional.of(createIcesiUser()));

        try {
            userService.save(icesiUserDTO1());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("User with this phone number already exists", messageOfException);
        }
    }

    @Test
    public void testIcesiUserWithPhoneNumberAndEmailAlreadyExists() {
        userService.save(icesiUserDTO1());
        try {
            userService.save(icesiUserDTO());

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
            assertEquals("USER ROLE CANNOT BE NULL", messageOfException);
        }
    }

    private IcesiUserDTO icesiUserDTO() {
        return IcesiUserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .icesiRole(createRole())
                .build();
    }

    private IcesiUserDTO icesiUserDTO1() {
        return IcesiUserDTO.builder()
                .firstName("John")
                .lastName("AR")
                .email("example@exampleEmail.com")
                .phoneNumber("1288567")
                .icesiRole(createRole())
                .build();
    }

    private IcesiUserDTO createIcesiUserDTOWithoutRole() {
        return IcesiUserDTO.builder()
                .firstName("John")
                .lastName("AR")
                .email("example@exampleEmail.com")
                .phoneNumber("1288567")
                .build();
    }

    private IcesiUser createIcesiUser() {
        return IcesiUser.builder()
                .firstName("John")
                .lastName("AR")
                .email("example@exampleEmail.com")
                .phoneNumber("1288567")
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
