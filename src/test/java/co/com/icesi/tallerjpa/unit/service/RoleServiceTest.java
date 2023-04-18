package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.dto.RoleDTO;
import co.com.icesi.tallerjpa.error.custom.ExistsException;
import co.com.icesi.tallerjpa.mapper.RoleMapper;
import co.com.icesi.tallerjpa.mapper.RoleMapperImpl;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.service.RoleService;
import co.com.icesi.tallerjpa.unit.matcher.RoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    private RoleService roleService;
    private RoleRepository roleRepository;

    private RoleMapper roleMapper;

    @BeforeEach
    public void setup() {
       roleRepository = mock(RoleRepository.class);
       roleMapper = spy(RoleMapperImpl.class);
       roleService = new RoleService(roleRepository, roleMapper);
    }

    @Test
    public void testCreateRole() {
        roleService.save(defaultRoleDTO());
        verify(roleMapper, times(1)).fromRoleDTO(any());
        verify(roleRepository, times(1)).save(argThat(new RoleMatcher(defaultRole())));
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExist(){
        when(roleRepository.existsByName(any())).thenReturn(true);
        try{
            roleService.save(defaultRoleDTO());
        }catch (ExistsException e){
            assertEquals("Name already exists", e.getMessage());
        }
    }

    private RoleDTO defaultRoleDTO() {
        return RoleDTO.builder()
                .name("name")
                .description("description")
                .build();
    }

    private Role defaultRole() {
        return Role.builder()
                .name("name")
                .description("description")
                .build();
    }

}
