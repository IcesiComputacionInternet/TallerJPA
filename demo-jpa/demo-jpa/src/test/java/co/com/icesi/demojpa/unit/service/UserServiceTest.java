package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.mapper.UserMapperImpl;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserMapper userMapper;

    private UserRepository userRespository;

    private RoleRepository roleRepository;

    private UserService userService;

    @BeforeEach
    public void setup() {
        userMapper = spy(UserMapperImpl.class);
        userRespository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);

        userService = new UserService(userMapper, userRespository, roleRepository);
    }

    private UserCreateDTO defaultUserDTO() {
        return UserCreateDTO.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("test@example.com")
                .phone("123456789")
                .password("123456")
                .roleName("USER")
                .build();
    }

    private IcesiUser defaultUser() {
        return IcesiUser.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("test@example.com")
                .phoneNumber("123456789")
                .password("123456")
                .role(defaultRole())
                .build();

    }

    private IcesiRole defaultRole() {
        return IcesiRole.builder()
                .name("USER")
                .description("User")
                .build();
    }


    @Test
    public void testSaveUser() {

        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultRole()));
        when(userRespository.existsByEmail(anyString())).thenReturn(false);
        when(userRespository.existsByPhone(anyString())).thenReturn(false);
        when(userMapper.fromIcesiUser(any(UserCreateDTO.class))).thenReturn(defaultUser());

        userService.save(defaultUserDTO());

        verify(roleRepository, times(1)).findByName(anyString());
        verify(userRespository, times(1)).existsByEmail(anyString());
        verify(userRespository, times(1)).existsByPhone(anyString());
        verify(userMapper, times(1)).fromIcesiUser(any(UserCreateDTO.class));
        verify(userRespository, times(1)).save(any(IcesiUser.class));


    }

    @Test
    public void testSaveUserWhenEmailAlreadyExists() {
        when(userRespository.existsByEmail(any())).thenReturn(true);
        try {
            userService.save(defaultUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Email is repeated", message);
        }
    }

    @Test
    public void testSaveUserWhenPhoneNumberAlreadyExists() {
        when(userRespository.existsByPhone(any())).thenReturn(true);
        try {
            userService.save(defaultUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Phone is repeated", message);
        }
    }

    @Test
    public void testSaveUserWhenEmailAndPhoneNumberAlreadyExists() {
        when(userRespository.existsByEmail(any())).thenReturn(true);
        when(userRespository.existsByPhone(any())).thenReturn(true);
        try {
            userService.save(defaultUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Email and phone are repeated", message);
        }
    }

    @Test
    public void testSaveUserWhenRoleDoesNotExist() {
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());
        try {
            userService.save(defaultUserDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Role not found", message);
        }
    }


}
