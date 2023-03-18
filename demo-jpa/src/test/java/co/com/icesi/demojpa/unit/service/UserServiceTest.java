package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.service.UserService;
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
    private UserMapper userMapper;
    private RoleRepository roleRepository;

    @BeforeEach
    public void init(){
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userMapper = spy(UserMapper.class);
        userService = new UserService(userRepository, userMapper, roleRepository);
    }

    @Test
    public void testSaveUser(){

        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));
        userService.save(defaultCreateUserDTO());
        IcesiUser expectedUser = defaultCreateUser();

        verify(userRepository, times(1)).save(argThat(new UserMatcher(expectedUser)));
    }

    @Test
    public void testSaveUserEmailAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultCreateUser()));
        try{
            userService.save(defaultCreateUserDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User with this e-mail already exists",message);
        }
    }

    @Test
    public void testSaveUserPhoneNumberAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));

        when(userRepository.findByPhone(any())).thenReturn(Optional.of(defaultCreateUser()));
        try{
            userService.save(defaultCreateUserDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User with this phone number already exists",message);
        }
    }

    @Test
    public void testSaveUserEmailAndPhoneNumberAlreadyExist(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultCreateUser()));
        when(userRepository.findByPhone(any())).thenReturn(Optional.of(defaultCreateUser()));
        try{
            userService.save(defaultCreateUserDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User with both e-mail and phone already exists",message);
        }
    }

    @Test
    public void testSaveUserWithNullRole(){
        UserCreateDTO userCreateDTO = UserCreateDTO.builder().userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("example@example.com")
                .password("123456")
                .phoneNumber("1234567890")
                .build();
        try{
            userService.save(userCreateDTO);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User must have a role",message);
        }
    }

    @Test
    public void testSaveUserRoleNotExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());
        try{
            userService.save(defaultCreateUserDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Role does not exist",message);
        }
    }


    private IcesiUser defaultCreateUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("example@example.com")
                .password("123456")
                .phoneNumber("1234567890")
                .role(defaultCreateRole())
                .build();
    }

    private UserCreateDTO defaultCreateUserDTO(){
        return UserCreateDTO.builder()
            .userId(UUID.randomUUID())
            .firstName("John")
            .lastName("Doe")
            .email("example@example.com")
            .password("123456")
            .phoneNumber("1234567890")
            .role(defaultCreateRoleDTO())
            .build();
    }

    private RoleCreateDTO defaultCreateRoleDTO(){
        return RoleCreateDTO.builder()
            .roleId(UUID.randomUUID())
            .name("ROLE_USER")
            .build();
    }

    private IcesiRole defaultCreateRole(){
        return IcesiRole.builder()
            .roleId(UUID.randomUUID())
            .name("ROLE_USER")
            .build();
    }
}
