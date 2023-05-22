package co.com.icesi.TallerJPA.unit.service;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.error.exception.ArgumentsException;
import co.com.icesi.TallerJPA.mapper.RoleMapper;
import co.com.icesi.TallerJPA.mapper.RoleMapperImpl;
import co.com.icesi.TallerJPA.mapper.responseMapper.UserResponseMapper;
import co.com.icesi.TallerJPA.mapper.responseMapper.UserResponseMapperImpl;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import co.com.icesi.TallerJPA.service.RoleService;
import co.com.icesi.TallerJPA.unit.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleServiceTest {
    private RoleService roleService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserResponseMapper userResponseMapper;
    private RoleMapper roleMapper;

    @BeforeEach
    public void init(){
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        userRepository = mock(UserRepository.class);
        userResponseMapper = spy(UserResponseMapperImpl.class);
        roleService = new RoleService(roleRepository,roleMapper,userRepository,userResponseMapper);
    }

    @Test
    public void testCreateRole(){
        when(roleRepository.findByName(any())).thenReturn(false);
        roleService.save(defaultRoleCreateDTO());
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(defaultRole())));
        verify(roleMapper, times(1)).fromIcesiRoleDTO(any());
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(true);
        try{
            roleService.save(defaultRoleCreateDTO());
        }catch (ArgumentsException e){
            String message = e.getMessage();
            assertEquals("Existing data",message);
        }
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
                .description("Ninguna")
                .build();
    }
}
