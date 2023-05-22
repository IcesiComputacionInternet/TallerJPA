package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.error.exception.CustomException;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.mapper.UserMapperImpl;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.service.UserService;
import co.com.icesi.tallerjpa.unit.matcher.UserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    private  RoleRepository roleRepository;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userMapper = spy(UserMapperImpl.class);

        userService = new UserService(userRepository, userMapper, roleRepository);
    }

    @Test
    public void testCreateUser(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultRole()));

        userService.save(defaultUserDTO(), "ADMIN");
        verify(roleRepository, times(1)).findByName(any());
        verify(userMapper, times(1)).fromUserDTO(any());
        verify(userRepository, times(1)).save(argThat(new UserMatcher(defaultIcesiUser())));
    }

    @Test
    public void testCreateUserWhenRoleIsNull(){
        RequestUserDTO userDTO = defaultUserDTO();
        userDTO.setRole(null);

        try{
            userService.save(userDTO, "ADMIN");
        }catch (Exception e){
            assertEquals("Role not found", e.getMessage());
        }

    }
    @Test
    public void testCreateUserWhenRoleDoesNotExist(){
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());

        try{
            userService.save(defaultUserDTO(), "ADMIN");
        }catch (Exception e){
            assertEquals("Role not found", e.getMessage());
        }

    }
    @Test
    public void testCreateUserWhenEmailAlreadyExist(){
        when(userRepository.existsByEmail(any())).thenReturn(true);

        try{
            userService.save(defaultUserDTO(), "ADMIN");
        }catch (CustomException e){
            assertEquals("Email already exists", e.getMessage());
        }

    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExist(){
        when(userRepository.existsByPhoneNumber(any())).thenReturn(true);

        try{
            userService.save(defaultUserDTO(), "ADMIN");
        }catch (CustomException e){
            assertEquals("Phone number already exists", e.getMessage());
        }

    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExist(){
        when(userRepository.existsByPhoneNumber(any())).thenReturn(true);
        when(userRepository.existsByEmail(any())).thenReturn(true);

        try{
            userService.save(defaultUserDTO(), "ADMIN");
        }catch (CustomException e){
            assertEquals("Email and Phone is already used", e.getMessage());
        }

    }

    //Bank users can only create users
    @Test
    public void testCreateUserWhenRoleIsBank(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultRole()));

        userService.save(defaultUserDTO(), "BANK");
        verify(roleRepository, times(1)).findByName(any());
        verify(userMapper, times(1)).fromUserDTO(any());
        verify(userRepository, times(1)).save(argThat(new UserMatcher(defaultIcesiUser())));
    }

    @Test
    public void testCreateUserWhenRoleIsBankAndUserRolNotIsUser(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultRole()));

        var user = defaultUserDTO();
        user.setRole("ADMIN");

        try{
            userService.save(user, "BANK_USER");
        }catch (CustomException e){
            assertEquals("Bank users can only create users", e.getMessage());
        }

    }


    private RequestUserDTO defaultUserDTO(){
        return RequestUserDTO.builder()
                .firstName("Arturo")
                .lastName("Diaz")
                .email("prueba@gmail.com")
                .phoneNumber("12345")
                .password("12345")
                .role("USER")
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Arturo")
                .lastName("Diaz")
                .email("prueba@gmail.com")
                .phoneNumber("12345")
                .password("12345")
                .role(defaultRole())
                .build();
    }

    private Role defaultRole(){
        return Role.builder()
                .roleId(UUID.randomUUID())
                .name("USER")
                .description("Ingeniero de sistemas")
                .build();
    }
}
