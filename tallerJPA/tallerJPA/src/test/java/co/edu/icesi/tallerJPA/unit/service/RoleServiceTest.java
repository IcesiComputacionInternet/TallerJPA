package co.edu.icesi.tallerJPA.unit.service;

import co.edu.icesi.tallerJPA.dto.RoleDTO;
import co.edu.icesi.tallerJPA.exception.ExistingException;
import co.edu.icesi.tallerJPA.mapper.RoleMapper;
import co.edu.icesi.tallerJPA.model.Role;
import co.edu.icesi.tallerJPA.repository.RoleRepository;
import co.edu.icesi.tallerJPA.service.RoleService;
import co.edu.icesi.tallerJPA.unit.matcher.RoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    private RoleService roleService;
    private RoleRepository roleRepository;

    private RoleMapper roleMapper;

    @BeforeEach
    public void init() {
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapper.class);
        roleService = new RoleService(roleRepository, roleMapper);
    }

    @Test
    public void testSaveRole() {
        roleService.save(roleDTOByDefault());
        verify(roleMapper, times(1)).fromRoleDTO(any());
        verify(roleRepository, times(1)).save(argThat(new RoleMatcher(roleByDefault())));
    }

    @Test
    public void testSaveRoleWhenExistingName(){
        when(roleRepository.isByname(any())).thenReturn(true);
        try{
            roleService.save(roleDTOByDefault());
        }catch (ExistingException e){
            assertEquals("Role name exists, please provide other", e.getMessage());
        }
    }

    private RoleDTO roleDTOByDefault() {
        return RoleDTO.builder()
                .name("Rol1")
                .description("This is the first rol")
                .build();
    }

    private Role roleByDefault() {
        return Role.builder()
                .name("Rol1")
                .description("This is the first rol")
                .build();
    }

}
