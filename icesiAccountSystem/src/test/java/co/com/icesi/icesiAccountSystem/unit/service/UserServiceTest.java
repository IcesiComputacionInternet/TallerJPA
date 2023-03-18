package co.com.icesi.icesiAccountSystem.unit.service;

import co.com.icesi.icesiAccountSystem.dto.UserDTO;
import co.com.icesi.icesiAccountSystem.mapper.UserMapper;
import co.com.icesi.icesiAccountSystem.mapper.UserMapperImpl;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import co.com.icesi.icesiAccountSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        userService.saveUser(defaultUserDTO());
        IcesiUser newIcesiUser = IcesiUser.builder()
                .role(defaultIcesiRole())
                .firstName("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .phoneNumber("3152485689")
                .password("taEkbs08")
                .build();
        verify(userRepository,times( 1)).save(argThat(new IcesiUserMatcher(newIcesiUser)));
    }

    @Test
    public void testCreateUserWithoutRole(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        try {
            userService.saveUser(defaultUserDTO1());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("It is not possible to create a user without role.", message);
        }
    }

    @Test
    public void testCreateUserWithRoleThatDoesNotExists(){
        try {
            userService.saveUser(defaultUserDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Role does not exists.", message);
        }
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try {
            userService.saveUser(defaultUserDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Another user already has this email and phone number.", message);
        }
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try {
            userService.saveUser(defaultUserDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Another user already has this email.", message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneAlreadyExists(){
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try {
            userService.saveUser(defaultUserDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Another user already has this phone number.", message);
        }
    }

    private UserDTO defaultUserDTO(){
        return UserDTO.builder()
                .roleName("Director SIS")
                .firstName("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .phoneNumber("3152485689")
                .password("taEkbs08")
                .build();
    }

    private UserDTO defaultUserDTO1(){
        return UserDTO.builder()
                .roleName("")
                .firstName("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .phoneNumber("3152485689")
                .password("taEkbs08")
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .role(defaultIcesiRole())
                .firstName("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .phoneNumber("3152485689")
                .password("taEkbs08")
                .build();
    }

    private IcesiRole defaultIcesiRole() {
        return IcesiRole.builder()
                .description("Director del programa de Ingenieria de sistemas")
                .name("Director SIS")
                .build();
    }
}
