package com.example.demo.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiRoleDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;
import com.example.demo.error.exception.IcesiException;
import com.example.demo.mapper.IcesiUserMapper;
import com.example.demo.mapper.IcesiUserMapperImpl;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import com.example.demo.model.enums.TypeIcesiRole;
import com.example.demo.repository.IcesiRoleRepository;
import com.example.demo.repository.IcesiUserRepository;
import com.example.demo.service.IcesiUserService;
import com.example.demo.unit.service.matchers.IcesiUserMatcher;

public class IcesiUserServiceTest {
    private IcesiUserService icesiUserService;

    private IcesiUserRepository icesiUserRepository;

    private IcesiUserMapper icesiUserMapper;

    private IcesiRoleRepository icesiRoleRepository;

    @BeforeEach
    private void init() {
        icesiUserMapper = spy(IcesiUserMapperImpl.class);
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiUserService =  new IcesiUserService(icesiUserRepository, icesiUserMapper, icesiRoleRepository);
    }

    private IcesiUser defaultIcesiUser() {
        return IcesiUser.builder()
            .firstName("John")
            .lastName("Doe")
            .email("testEmail@example.com")
            .phoneNumber("999999")
            .password("1234")
            .icesiRole(defaultIcesiRole())
            .build();
    }

    private IcesiUserCreateDTO defaultIcesiUserCreateDTO() {
        return IcesiUserCreateDTO.builder()
            .firstName("John")
            .lastName("Doe")
            .email("testEmail@example.com")
            .phoneNumber("999999")
            .password("1234")
            .icesiRoleCreateDTO(defaultIcesiRoleDTO())
            .build();
    }

    private ResponseIcesiUserDTO defaultResponseIcesiUserDTO() {
        return ResponseIcesiUserDTO.builder()
        .firstName("John")
        .lastName("Doe")
        .email("testEmail@example.com")
        .phoneNumber("999999")
        .password("1234")
        .icesiRoleCreateDTO(defaultResponseIcesiRoleDTO())
        .build();
    }

    private IcesiRoleCreateDTO defaultIcesiRoleDTO() {
        return IcesiRoleCreateDTO.builder()
            .description("This role is for students")
            .name("student")
            .build();
    }

    private ResponseIcesiRoleDTO defaultResponseIcesiRoleDTO() {
        return ResponseIcesiRoleDTO.builder()
            .description("This role is for students")
            .name("student")
            .build();
    }

    private IcesiRole defaultIcesiRole() {
        return IcesiRole.builder()
            .description("This role is for students")
            .name("student")
            .build();
    }


    @Test
    public void testCreateIcesiUserWithRegisteredRoleAsAdmin() {
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        icesiUserService.create(TypeIcesiRole.admin.name(),defaultIcesiUserCreateDTO());
        IcesiUser icesiUser1 = IcesiUser.builder()
            .firstName("John")
            .lastName("Doe")
            .email("testEmail@example.com")
            .phoneNumber("999999")
            .password("1234")
            .icesiRole(defaultIcesiRole())
            .build();
        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser1)));
    }

    @Test
    public void testCreateIcesiUserWithRegisteredRoleAsBank() {
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        icesiUserService.create(TypeIcesiRole.bank.name(),defaultIcesiUserCreateDTO());
        IcesiUser icesiUser1 = IcesiUser.builder()
            .firstName("John")
            .lastName("Doe")
            .email("testEmail@example.com")
            .phoneNumber("999999")
            .password("1234")
            .icesiRole(defaultIcesiRole())
            .build();
        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser1)));
    }

    @Test
    public void testCreateIcesiUserWithRegisteredRoleAsUser() {
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        icesiUserService.create(TypeIcesiRole.user.name(),defaultIcesiUserCreateDTO());
        IcesiUser icesiUser1 = IcesiUser.builder()
            .firstName("John")
            .lastName("Doe")
            .email("testEmail@example.com")
            .phoneNumber("999999")
            .password("1234")
            .icesiRole(defaultIcesiRole())
            .build();
        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser1)));
    }

    @Test
    public void testCreateIcesiUserWithANoRegisteredRole() {
        try {
            icesiUserService.create(TypeIcesiRole.admin.name(),defaultIcesiUserCreateDTO());
            fail();
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("This role is not present in the database", message);
        }
    
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try {
            icesiUserService.create(TypeIcesiRole.admin.name(),defaultIcesiUserCreateDTO());
            fail();
        } catch(IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("This email is already in use", message);
        }

    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists() {
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try {
            icesiUserService.create(TypeIcesiRole.admin.name(),defaultIcesiUserCreateDTO());
            fail();
        } catch(IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("This phone number is already in use", message);
        }

    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExists() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
     
        try {
            icesiUserService.create(TypeIcesiRole.admin.name(),defaultIcesiUserCreateDTO());
            fail();
        } catch(IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("These email and phone number are already in use", message);
        }

    }

    @Test
    public void testFindIcesiUserByEmailWhenIsPresent() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiUser result = icesiUserService.findIcesiUserByEmail(defaultIcesiUser().getEmail());
        verify(icesiUserRepository, times(1)).findByEmail(any());
        assertEquals(defaultResponseIcesiUserDTO().getEmail(), result.getEmail());
    }

    @Test
    public void testFindIcesiUserByEmailWhenIsNotPresent() {
        try {
            icesiUserService.findIcesiUserByEmail(defaultIcesiUser().getEmail());
            fail();
        } catch(IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("This email is not present in the database", message);
        }
    }

    @Test
    public void testFindIcesiRoleByNameWhenIsPresent() {
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        IcesiRole result = icesiUserService.findIcesiRoleByName(defaultIcesiRole().getName());
        verify(icesiRoleRepository, times(1)).findByName(any());
        assertEquals(defaultResponseIcesiRoleDTO().getName(), result.getName());
    }

    @Test
    public void testFindIcesiRoleByNameWhenIsNotPresent() {
        try {
            icesiUserService.findIcesiRoleByName(defaultIcesiRole().getName());
            fail();
        } catch(IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("This role is not present in the database", message);
        }
    }

    @Test
    public void testUpdateIcesiRoleToAdminAsAdmin() {

    }

    @Test
    public void testUpdateIcesiRoleToAdminAsBank() {

    }
    @Test
    public void testUpdateIcesiRoleToAdminAsUser() {

    }

    @Test
    public void testUpdateIcesiRoleAsBank() {

    }

    @Test
    public void testUpdateIcesiRoleAsUser() {

    }

}
