package com.example.TallerJPA.unit.service;

import com.example.TallerJPA.dto.RoleCreateDTO;
import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.mapper.RoleMapper;
import com.example.TallerJPA.mapper.RoleMapperImpl;
import com.example.TallerJPA.mapper.UserMapper;
import com.example.TallerJPA.mapper.UserMapperImpl;
import com.example.TallerJPA.model.IcesiRole;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.repository.RoleRepository;
import com.example.TallerJPA.repository.UserRepository;
import com.example.TallerJPA.service.RoleService;
import com.example.TallerJPA.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserMapper userMapper;
    private UserRepository userRepository;
    private RoleService roleService;
    private RoleMapper roleMapper;
    private RoleRepository roleRepository;
    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userMapper = spy(UserMapperImpl.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleMapper,roleRepository);
        userService = new UserService(userRepository,userMapper,roleService);
    }

    @Test
    public void testCreateUser(){
        when(roleService.findRoleByName(any())).thenReturn(Optional.of(IcesiRole.builder().name("Estudiante").build()));
        userService.save(defaultUserCreateDTO());
        IcesiRole role = IcesiRole.builder()
                .name("Estudiante")
                .build();
        IcesiUser user = IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .password("1234")
                .phoneNumber("123456789")
                .role(role)
                .build();

        verify(userRepository,times(1)).save(argThat(new IcesiUserMatcher(user)));
    }
    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        assertThrows(RuntimeException.class,() -> userService.save(defaultUserCreateDTO()));
    }
    @Test
    public void testCreateUserWhenPhoneAlreadyExists(){
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        assertThrows(RuntimeException.class,() -> userService.save(defaultUserCreateDTO()));
    }
    @Test
    public void testCreateUserWhenRoleIsNotFound(){
        UserCreateDTO userCreateDTO = defaultUserCreateDTO();
        userCreateDTO.setRoleName("Seguridad");
        assertThrows(RuntimeException.class,() -> userService.save(userCreateDTO));
    }



    private IcesiUser defaultIcesiUser() {
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .password("1234")
                .phoneNumber("123456789").build();
    }


    private UserCreateDTO defaultUserCreateDTO() {
        return UserCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .phoneNumber("123456789")
                .password("1234")
                .roleName("Estudiante")
                .build();
    }
}
