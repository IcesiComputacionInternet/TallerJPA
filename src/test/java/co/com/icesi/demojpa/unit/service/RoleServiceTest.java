package co.com.icesi.demojpa.unit.service;
import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.mapper.RoleMapperImpl;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.servicio.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
public class RoleServiceTest {

    private RoleMapper roleMapper;

    private RoleService roleService;

    private RoleRepository roleRepository;

    private UserRepository userRepository;


    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleRepository,roleMapper, userRepository);
    }


    @Test
    public void testCreateRole(){
        roleService.save(defaultRoleDTO());
        IcesiRole icesiRole = defaultRole();
        verify(roleRepository,times(1)).save(argThat(new IcesiRoleMatcher(icesiRole)));
    }

    @Test
    public void testAddUser(){
        IcesiUser user = defaultIcesiUser();
        IcesiRole role =defaultRole();
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));
        roleService.addUser(role,user.getUserId());
        assertEquals(role.getUser(),roleRepository.findByName(role.getName()).get().getUser());
    }

    @Test
    public void testAddWithNoUser(){
        IcesiUser user = defaultIcesiUser();
        IcesiRole role =defaultRole();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        try{
            roleService.addUser(role,user.getUserId());
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe un usuario con esta id",exception.getMessage());
        }
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultRole()));
        try{
            roleService.save(defaultRoleDTO());
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Ya existe un rol con este nombre",message);
        }
    }

    private RoleCreateDTO defaultRoleDTO(){
        return RoleCreateDTO.builder()
                .description("Rol de prueba")
                .name("nombre de rol de prueba")
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("nombre de rol de prueba")
                .description("Rol de prueba")
                .user(new ArrayList<>())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .email("5")
                .firstName("John")
                .lastname("Doe")
                .phone("123")
                .password("123")
                .role(IcesiRole.builder()
                        .name("rol de prueba")
                        .description("rol de prueba")
                        .build())
                .build();
    }
}
