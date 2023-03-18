package co.com.icesi.icesiAccountSystem.unit.service;

import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapper;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapperImpl;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    private RoleService roleService;
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    @BeforeEach
    private void init(){
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleRepository, roleMapper);
    }

    @Test
    public void testCreateRole(){
        roleService.saveRole(defaultRoleDTO());
        IcesiRole newIcesiRole = IcesiRole.builder()
            .description("Director del programa de Diseño de Medios Interactivos")
            .name("Director DMI")
            .build();
        verify(roleRepository,times(1)).save(argThat(new IcesiRoleMatcher(newIcesiRole)));
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        try {
            roleService.saveRole(defaultRoleDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Another role already has this name.", message);
        }
    }

    private RoleDTO defaultRoleDTO() {
        return RoleDTO.builder()
                .description("Director del programa de Diseño de Medios Interactivos")
                .name("Director DMI")
                .build();
    }

    private IcesiRole defaultIcesiRole() {
        return IcesiRole.builder()
                .description("Director del programa de Ingenieria de sistemas")
                .name("Director SIS")
                .build();
    }
}
