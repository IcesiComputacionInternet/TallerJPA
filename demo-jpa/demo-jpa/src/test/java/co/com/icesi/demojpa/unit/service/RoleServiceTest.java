package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.request.RoleCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.mapper.RoleMapperImpl;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.service.RoleService;
import co.com.icesi.demojpa.unit.matcher.IcesiRoleMatcher;
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
        roleService = new RoleService(roleRepository,roleMapper);
    }

    @Test
    public void testSave(){
        roleService.save(defaultRoleDTO());
        IcesiRole role = defaultRole();
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(role)));

    }

    @Test
    public void testSaveWhenNameAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultRole()));
        try{
            roleService.save(defaultRoleDTO());
            fail();
        }catch (RuntimeException exception){
            String exceptionMessage = exception.getMessage();
            assertEquals("Role already exists",exceptionMessage);
        }
    }

    @Test
    public void testAddUserToRoleWhenUserDoesNotExist(){
        IcesiUser user = defaultIcesiUser();
        IcesiRole role =defaultRole();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        try{
            fail();
        }catch (RuntimeException exception){
            String exceptionMessage = exception.getMessage();
            assertEquals("User doesn't exists",exceptionMessage);
        }
    }

    private RoleCreateDTO defaultRoleDTO(){
        return RoleCreateDTO.builder()
                .description("test role")
                .name("investor")
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("investor")
                .description("test role")
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("5")
                .firstName("Maki")
                .lastName("Doe")
                .phoneNumber("123")
                .password("123")
                .accounts(new ArrayList<>())
                .role(IcesiRole.builder()
                        .name("rol de prueba")
                        .description("rol de prueba")
                        .build())
                .build();
    }
}
