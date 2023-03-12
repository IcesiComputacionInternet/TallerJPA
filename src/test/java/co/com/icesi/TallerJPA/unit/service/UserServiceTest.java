package co.com.icesi.TallerJPA.unit.service;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.exception.ArgumentsException;
import co.com.icesi.TallerJPA.mapper.UserMapper;
import co.com.icesi.TallerJPA.mapper.UserMapperImpl;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import co.com.icesi.TallerJPA.service.UserService;
import co.com.icesi.TallerJPA.unit.service.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        userService = new UserService(userRepository,roleRepository,userMapper);
    }

/*
    @Test
    public void testCreateUser() {
        userService.save(defaultUserCreateDTO());
        verify(userMapper, times(1)).fromIcesiUserDTO(any());
        verify(userRepository, times(1)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));

    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(true);
        assertThrows(ArgumentsException.class,() -> userService.save(defaultUserCreateDTO()));
    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists(){
        when(userRepository.findByPhoneNumber(any())).thenReturn(true);
        assertThrows(ArgumentsException.class,() -> userService.save(defaultUserCreateDTO()));
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(true);
        when(userRepository.findByPhoneNumber(any())).thenReturn(true);
        assertThrows(ArgumentsException.class,() -> userService.save(defaultUserCreateDTO()));
    }

    @Test
    public void testCreateUserWhenRoleIsNull(){
        UserCreateDTO userCreateDTO = defaultUserCreateDTO();
        userCreateDTO.setRole(null);
        assertThrows(ArgumentsException.class,() -> userService.save(userCreateDTO));
    }

    @Test
    public void testCreateUserWhenRoleIsNotNull(){
        UserCreateDTO userCreateDTO = defaultUserCreateDTO();
        userCreateDTO.setRole(new IcesiRole());
        userService.save(userCreateDTO);
        verify(userMapper, times(1)).fromIcesiUserDTO(any());
        verify(userRepository, times(1)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));
    }

    private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("1234")
                .password("1234")
                .role(new IcesiRole())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("1234")
                .password("1234")
                .role(new IcesiRole())
                .build();
    }*/

}
