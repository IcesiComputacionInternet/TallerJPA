package co.com.icesi.icesiAccountSystem.unit.service;


import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.mapper.UserMapper;
import co.com.icesi.icesiAccountSystem.mapper.UserMapperImpl;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import co.com.icesi.icesiAccountSystem.service.UserService;
import co.com.icesi.icesiAccountSystem.unit.service.matcher.IcesiUserMatcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

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
        // Arrange
        var userDTO = defaultUserDTO();
        var icesiRole= defaultIcesiRole();
        when(roleRepository.findByName(any())).thenReturn(Optional.of(icesiRole));
        // Act
        userService.saveUser(userDTO);
        // Assert
        IcesiUser newIcesiUser = IcesiUser.builder()
                .role(defaultIcesiRole())
                .firstName("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .phoneNumber("3152485689")
                .password("taEkbs08")
                .build();
        verify(userRepository, times(1)).findByEmail(any());
        verify(userRepository, times(1)).findByPhoneNumber(any());
        verify(roleRepository, times(1)).findByName(any());
        verify(userRepository,times( 1)).save(argThat(new IcesiUserMatcher(newIcesiUser)));
        verify(userMapper, times(1)).fromUserDTO(any());
        verify(userMapper, times(1)).fromUserToResponseUserDTO(any());
    }

    @Test
    public void testCreateUserWithoutRole(){
        // Arrange
        var userDTO = defaultUserDTO1();
        try {
            // Act
            userService.saveUser(userDTO);
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            // Assert
            assertEquals("Role was not specified or does not exist yet.", message);
        }
    }

    @Test
    public void testCreateUserWithRoleThatDoesNotExists(){
        // Arrange
        var userDTO = defaultUserDTO();
        try {
            // Act
            userService.saveUser(userDTO);
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            // Assert
            assertEquals("Role was not specified or does not exist yet.", message);
        }
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneAlreadyExists(){
        // Arrange
        var userDTO = defaultUserDTO();
        var icesiRole= defaultIcesiRole();
        var icesiUser= defaultIcesiUser();
        when(roleRepository.findByName(any())).thenReturn(Optional.of(icesiRole));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(icesiUser));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(icesiUser));
        try {
            // Act
            userService.saveUser(userDTO);
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            // Assert
            assertEquals("A User with the same email and phone already exists.", message);
        }
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        // Arrange
        var userDTO = defaultUserDTO();
        var icesiRole= defaultIcesiRole();
        var icesiUser= defaultIcesiUser();
        when(roleRepository.findByName(any())).thenReturn(Optional.of(icesiRole));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(icesiUser));
        try {
            // Act
            userService.saveUser(userDTO);
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            // Assert
            assertEquals("A User with the same email already exists.", message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneAlreadyExists(){
        // Arrange
        var userDTO = defaultUserDTO();
        var icesiRole= defaultIcesiRole();
        var icesiUser= defaultIcesiUser();
        when(roleRepository.findByName(any())).thenReturn(Optional.of(icesiRole));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(icesiUser));
        try {
            // Act
            userService.saveUser(userDTO);
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            // Assert
            assertEquals("A User with the same phone already exists.", message);
        }
    }

    private RequestUserDTO defaultUserDTO(){
        return RequestUserDTO.builder()
                .roleName("Director SIS")
                .firstName("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .phoneNumber("3152485689")
                .password("taEkbs08")
                .build();
    }

    private RequestUserDTO defaultUserDTO1(){
        return RequestUserDTO.builder()
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
                .userId(UUID.randomUUID())
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
                .roleId(UUID.randomUUID())
                .description("Director del programa de Ingenieria de sistemas")
                .name("Director SIS")
                .build();
    }

}
