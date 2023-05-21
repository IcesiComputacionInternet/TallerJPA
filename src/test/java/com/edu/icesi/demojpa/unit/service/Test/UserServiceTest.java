package com.edu.icesi.demojpa.unit.service.Test;

import com.edu.icesi.demojpa.dto.request.RequestUserDTO;
import com.edu.icesi.demojpa.error.util.IcesiExceptionBuilder;
import com.edu.icesi.demojpa.mapper.UserMapper;
import com.edu.icesi.demojpa.mapper.UserMapperImpl;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.RoleRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import com.edu.icesi.demojpa.service.UserService;
import com.edu.icesi.demojpa.unit.service.Matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userMapper = spy(UserMapperImpl.class);
        userService = new UserService(userRepository, roleRepository, userMapper);
    }

    @Test
    public void testCreateUser(){
        when(roleRepository.findRoleByName(any())).thenReturn(Optional.ofNullable(defaultIcesiRole()));

        userService.save(defaultUserCreateDTO());
        IcesiUser icesiUserToCompare = defaultIcesiUser();

        verify(userRepository, times(1)).isEmailInUse(any());
        verify(userRepository, times(1)).isPhoneNumberInUse(any());
        verify(roleRepository, times(1)).findRoleByName(any());
        verify(userMapper, times(1)).fromIcesiUserDTO(any());
        verify(userRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUserToCompare)));
    }

    @Test
    public void testCreateUserWhenEmailExists(){
        when(userRepository.isEmailInUse(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(roleRepository.findRoleByName(any())).thenReturn(Optional.ofNullable(defaultIcesiRole()));
        try{
            userService.save(defaultUserCreateDTO());
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The email is already in use", message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneNumberExists(){
        when(userRepository.isPhoneNumberInUse(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(roleRepository.findRoleByName(any())).thenReturn(Optional.ofNullable(defaultIcesiRole()));
        try {
            userService.save(defaultUserCreateDTO());
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The phone-number is already in use", message);
        }
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberExists(){
        when(userRepository.isEmailInUse(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(userRepository.isPhoneNumberInUse(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(roleRepository.findRoleByName(any())).thenReturn(Optional.ofNullable(defaultIcesiRole()));
        try{
            userService.save(defaultUserCreateDTO());
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The email is already in use The phone-number is already in use", message);
        }
    }

    @Test
    public void testCreateUserWhenRoleDontExists(){
        try {
            userService.save(defaultUserCreateDTO());
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The role Student doesn't exist", message);
        }
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .phoneNumber("1234567890")
                .password("password")
                .role(defaultIcesiRole())
                .build();
    }

    private RequestUserDTO defaultUserCreateDTO(){
        return RequestUserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .phoneNumber("1234567890")
                .password("password")
                .roleType("Student")
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("Student")
                .description("Loreno Insomnio, nunca supe como se dice")
                .build();
    }
}
