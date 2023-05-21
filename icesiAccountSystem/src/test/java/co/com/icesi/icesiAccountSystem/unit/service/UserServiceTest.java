package co.com.icesi.icesiAccountSystem.unit.service;


import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemException;
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
        userService.saveUser(userDTO, "admin");
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
            userService.saveUser(userDTO, "admin");
            fail();
        } catch (AccountSystemException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail = details.get(0);
            // Assert
            assertEquals("Some fields of the new user had errors", message);
            assertEquals("ERR_404", detail.getErrorCode(), "Code doesn't match");
            assertEquals("Role with name:  not found.", detail.getErrorMessage(), "Error message doesn't match");
        }
    }

    @Test
    public void testCreateUserWithRoleThatDoesNotExists(){
        // Arrange
        var userDTO = defaultUserDTO();
        try {
            // Act
            userService.saveUser(userDTO, "admin");
            fail();
        } catch (AccountSystemException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail = details.get(0);
            // Assert
            assertEquals("Some fields of the new user had errors", message);
            assertEquals("ERR_404", detail.getErrorCode(), "Code doesn't match");
            assertEquals("Role with name: Director SIS not found.", detail.getErrorMessage(), "Error message doesn't match");
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
            userService.saveUser(userDTO, "admin");
            fail();
        } catch (AccountSystemException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(2, details.size());
            var detail1 = details.get(0);
            var detail2 = details.get(1);
            // Assert
            assertEquals("Some fields of the new user had errors", message);
            assertEquals("ERR_DUPLICATED", detail1.getErrorCode(), "Code doesn't match");
            assertEquals("ERR_DUPLICATED", detail2.getErrorCode(), "Code doesn't match");
            assertEquals("Resource user with field email: ykaar@gmail.com, already exists.", detail1.getErrorMessage(), "Error message doesn't match");
            assertEquals("Resource user with field phone: 3152485689, already exists.", detail2.getErrorMessage(), "Error message doesn't match");
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
            userService.saveUser(userDTO, "admin");
            fail();
        } catch (AccountSystemException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail1 = details.get(0);
            // Assert
            assertEquals("Some fields of the new user had errors", message);
            assertEquals("ERR_DUPLICATED", detail1.getErrorCode(), "Code doesn't match");
            assertEquals("Resource user with field email: ykaar@gmail.com, already exists.", detail1.getErrorMessage(), "Error message doesn't match");
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
            userService.saveUser(userDTO, "admin");
            fail();
        } catch (AccountSystemException exception){
            String message = exception.getMessage();
            var error = exception.getError();
            var details = error.getDetails();
            assertEquals(1, details.size());
            var detail1 = details.get(0);
            // Assert
            assertEquals("Some fields of the new user had errors", message);
            assertEquals("ERR_DUPLICATED", detail1.getErrorCode(), "Code doesn't match");
            assertEquals("Resource user with field phone: 3152485689, already exists.", detail1.getErrorMessage(), "Error message doesn't match");
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
