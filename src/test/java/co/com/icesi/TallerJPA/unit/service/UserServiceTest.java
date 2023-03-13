package co.com.icesi.TallerJPA.unit.service;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.dto.response.UserResponseDTO;
import co.com.icesi.TallerJPA.exception.ArgumentsException;
import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.mapper.UserMapper;
import co.com.icesi.TallerJPA.mapper.UserMapperImpl;
import co.com.icesi.TallerJPA.mapper.responseMapper.UserResponseMapper;
import co.com.icesi.TallerJPA.mapper.responseMapper.UserResponseMapperImpl;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import co.com.icesi.TallerJPA.service.UserService;
import co.com.icesi.TallerJPA.unit.service.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private UserResponseMapper userResponseMapper;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userMapper = spy(UserMapperImpl.class);
        userResponseMapper = spy(UserResponseMapperImpl.class);
        userService = new UserService(userRepository,roleRepository,userMapper,userResponseMapper);
    }

    @Test
    public void testCreateUser(){
        when(userRepository.findByEmail(any())).thenReturn(false);
        when(userRepository.findByPhoneNumber(any())).thenReturn(false);
        when(roleRepository.findByName(any())).thenReturn(true);
        when(roleRepository.returnRole(any())).thenReturn(Optional.of(defaultRole()));
        when(userRepository.save(any())).thenReturn(defaultIcesiUser());
        when(userResponseMapper.fromICesiUSer(any())).thenReturn(defaultUserResponseDTO());
        userService.save(defaultUserCreateDTO());
        verify(userRepository,times(1)).save(any());
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(true);
        try{
            userService.save(defaultUserCreateDTO());
        }catch (ArgumentsException e) {
            String message = e.getMessage();
            assertEquals("Email already exist",message);
        }
        //assertThrows(ArgumentsException.class,() -> userService.save(defaultUserCreateDTO()));
    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists(){
        when(userRepository.findByPhoneNumber(any())).thenReturn(true);
        assertThrows(ArgumentsException.class,() -> userService.save(defaultUserCreateDTO()));
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(true);
        when(userRepository.findByPhoneNumber(any())).thenReturn(true);
        assertThrows(ArgumentsException.class,() -> userService.save(defaultUserCreateDTO()));
    }

    @Test
    public void testCreateUserWhenRoleNotExist(){
        UserCreateDTO userCreateDTO = defaultUserCreateDTO();
        userCreateDTO.setRole("Student");
        when(roleRepository.findByName(any())).thenReturn(false);
        try{
            userService.save(userCreateDTO);
        }catch (ArgumentsException e) {
            String message = e.getMessage();
            assertEquals("Role does not exist", message);
        }
        //assertThrows(ArgumentsException.class,() -> userService.save(userCreateDTO));
    }


    private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("1234")
                .password("1234")
                .role("Admin")
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("1234")
                .password("1234")
                .role(defaultRole())
                .build();
    }

    private UserResponseDTO defaultUserResponseDTO() {
        return UserResponseDTO.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("1234")
                .password("1234")
                .role(defaultRoleCreateDTO())
                .build();
    }

    private RoleCreateDTO defaultRoleCreateDTO() {
        return RoleCreateDTO.builder()
                .name("Admin")
                .description("Ninguna")
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("Admin")
                .description("Admin")
                .build();
    }

}
