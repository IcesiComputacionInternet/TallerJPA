package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.exception.ExistsException;
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
        when(roleRepository.findByName(any())).thenReturn(defaultRole());

        userService.save(defaultUserDTO());
        verify(roleRepository, times(1)).findByName(any());
        verify(userMapper, times(1)).fromUserDTO(any());
        verify(userRepository, times(1)).save(argThat(new UserMatcher(defaultIcesiUser())));
    }
    @Test
    public void testCreateUserWhenEmailAlreadyExist(){
        when(userRepository.existsByEmail(any())).thenReturn(true);

        try{
            userService.save(defaultUserDTO());
        }catch (ExistsException e){
            assertEquals("Email already exists", e.getMessage());
        }

    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExist(){
        when(userRepository.existsByPhoneNumber(any())).thenReturn(true);

        try{
            userService.save(defaultUserDTO());
        }catch (ExistsException e){
            assertEquals("Phone number already exists", e.getMessage());
        }

    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExist(){
        when(userRepository.existsByPhoneNumber(any())).thenReturn(true);
        when(userRepository.existsByEmail(any())).thenReturn(true);

        try{
            userService.save(defaultUserDTO());
        }catch (ExistsException e){
            assertEquals("Email and Phone is already used", e.getMessage());
        }

    }

    private RequestUserDTO defaultUserDTO(){
        return RequestUserDTO.builder()
                .firstName("Arturo")
                .lastName("Diaz")
                .email("prueba@gmail.com")
                .phoneNumber("12345")
                .password("12345")
                .role("Ingeniero")
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
                .name("Ingeniero")
                .description("Ingeniero de sistemas")
                .build();
    }
}
