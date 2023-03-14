package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.mapper.UserMapperImpl;
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
        //assertThrows(RuntimeException.class,()->userService.save(defaultUserCreateDTO()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("User already exists",message);
        }

    }

    private UserCreateDTO defaultUserCreateDTO(){
     return UserCreateDTO.builder()
             .email("5")
             .firstName("John")
             .lastname("Doe")
             .isActive(true)
             .password("123")
             .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .email("5")
                .firstName("John")
                .lastname("Doe")
                .isActive(true)
                .password("123")
                .build();
    }

}
