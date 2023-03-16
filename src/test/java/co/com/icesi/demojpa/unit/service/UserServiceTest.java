package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.mapper.UserMapperImpl;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.servicio.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        userMapper = spy(UserMapperImpl.class);
        userService = new UserService(userRepository,userMapper);

    }

    @Test
    public void testCreateUser(){
        userService.save(defaultUserCreateDTO());
        IcesiUser icesiUser1 = defaultIcesiUser();
        verify(userRepository,times(1)).save(argThat(new IcesiUserMatcher(icesiUser1)));
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Ya hay un usuario con este email",message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneAlreadyExists(){
        when(userRepository.findByPhone(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Ya hay un usuario con este celular",message);
        }
    }

    @Test
    public void testCreateUserSinDTO(){

        try{
            userService.save(defaultUserCreateDTOSinRol());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("El usuario no tiene rol",message);
        }
    }

    private UserCreateDTO defaultUserCreateDTO(){
     return UserCreateDTO.builder()
             .email("5")
             .firstName("John")
             .lastname("Doe")
             .phone("123")
             .password("123")
             .role(RoleCreateDTO.builder()
                     .name("rol de prueba")
                     .description("rol de prueba")
                     .build())
             .build();
    }

    private UserCreateDTO defaultUserCreateDTOSinRol(){
        return UserCreateDTO.builder()
                .email("5")
                .firstName("John")
                .lastname("Doe")
                .phone("123")
                .password("123")
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .email("5")
                .firstName("John")
                .lastname("Doe")
                .phone("123")
                .password("123")
                .role(IcesiRole.builder()
                        .name("rol de prueba")
                        .description("rol de prueba")
                        .build())
                .build();
    }

    private IcesiUser defaultIcesiUserSinRol(){
        return IcesiUser.builder()
                .email("5")
                .firstName("John")
                .lastname("Doe")
                .phone("123")
                .password("123")
                .role(null)
                .build();
    }

}
