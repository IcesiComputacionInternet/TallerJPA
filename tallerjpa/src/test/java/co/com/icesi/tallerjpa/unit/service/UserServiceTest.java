package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.dto.UserCreateDTO;
import co.com.icesi.tallerjpa.mapper.UserMapperImpl;
import co.com.icesi.tallerjpa.model.IcesiRole;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        userMapper = spy(UserMapperImpl.class);
        roleRepository = mock(RoleRepository.class);
        userService = new UserService(userRepository, roleRepository, userMapper);
    }

    @Test
    public void testCreateUser(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        userService.save(defaultUserCreateDTO());
        IcesiUser icesiUser1 = IcesiUser.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .email("testEmail@example.com")
                .phoneNumber("1234567")
                .password("1234")
                .role(defaultIcesiRole())
                .build();
        verify(userRepository,times( 1)).save(argThat(new IcesiUserMatcher(icesiUser1)));
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        //assertThrows(RuntimeException.class, () -> userService.save(defaultUserCreateDTO()));
        try {
            userService.save(defaultUserCreateDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Email is already in use", message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists(){
        when(userRepository.findByPhone(any())).thenReturn(Optional.of(defaultIcesiUser()));
        //assertThrows(RuntimeException.class, () -> userService.save(defaultUserCreateDTO()));
        try {
            userService.save(defaultUserCreateDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Phone is already in use", message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneNumberAndEmailAlreadyExists(){
        when(userRepository.findByPhone(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        //assertThrows(RuntimeException.class, () -> userService.save(defaultUserCreateDTO()));
        try {
            userService.save(defaultUserCreateDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Email and Phone are already in use", message);
        }
    }

    @Test
    public void testCreateUserWhenRoleNotExist(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

        try {
            userService.save(defaultUserCreateDTONotRole());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("This role does not exist",message);
        }
    }

    private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .email("testEmail@example.com")
                .phoneNumber("1234567")
                .password("1234")
                .icesiroleDto(defaultRoleCreateDTO())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .email("testEmail@example.com")
                .phoneNumber("1234567")
                .password("1234")
                .role(defaultIcesiRole())
                .build();
    }

    private UserCreateDTO defaultUserCreateDTONotRole(){
        return UserCreateDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .email("testEmail@example.com")
                .phoneNumber("1234567")
                .password("1234")
                .build();
    }

    private RoleCreateDTO defaultRoleCreateDTO() {
        return RoleCreateDTO.builder()
                .description("no description")
                .name("Administrator")
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("no description")
                .name("Administrator")
                .build();
    }

}
