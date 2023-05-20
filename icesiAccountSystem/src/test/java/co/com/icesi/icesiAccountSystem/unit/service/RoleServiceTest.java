package co.com.icesi.icesiAccountSystem.unit.service;

import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapper;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapperImpl;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.service.RoleService;
import co.com.icesi.icesiAccountSystem.unit.service.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

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

    public void testCreateRole_HappyPath(){
        // Arrange
        var roleDTO= defaultRoleDTO();
        // Act
        roleService.saveRole(roleDTO);
        // Assert
        IcesiRole newIcesiRole = IcesiRole.builder()
            .description("Director del programa de Diseño de Medios Interactivos")
            .name("Director DMI")
            .build();
        verify(roleRepository,times(1)).save(argThat(new IcesiRoleMatcher(newIcesiRole)));
        verify(roleMapper, times(1)).fromRoleDTO(any());
        verify(roleMapper, times(1)).fromRoleToRoleDTO(any());
        verify(roleRepository, times(1)).findByName(any());
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
        // Arrange
        var roleDTO= defaultRoleDTO();
        var icesiRole= defaultIcesiRole();
        when(roleRepository.findByName(any())).thenReturn(Optional.of(icesiRole));

        try {
            // Act
            roleService.saveRole(roleDTO);
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            // Assert
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
                .roleId(UUID.randomUUID())
                .description("Director del programa de Diseño de Medios Interactivos")
                .name("Director DMI")
                .build();
    }
}
