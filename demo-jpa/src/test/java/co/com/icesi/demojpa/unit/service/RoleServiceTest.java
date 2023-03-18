package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    private RoleService roleService;
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    @BeforeEach
    public void setup(){
        roleRepository = mock(RoleRepository.class) ;
        roleMapper = spy(RoleMapper.class);
        roleService = new RoleService(roleRepository, roleMapper);
    }

    @Test
    public void testCreateRole(){
        RoleCreateDTO roleCreateDTO = defaultRoleCreateDTO();
        IcesiRole expectedRole = defaultRoleCreate();
        expectedRole.setRoleId(UUID.randomUUID());

        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(roleMapper.fromIcesiRoleDTO(roleCreateDTO)).thenReturn(expectedRole);
        when(roleRepository.save(expectedRole)).thenReturn(expectedRole);

        roleService.save(defaultRoleCreateDTO());
        verify(roleRepository,times(1)).save(argThat(new RoleMatcher(expectedRole)));
    }

    @Test
    public void saveRoleWithExistentName() {
        when(roleRepository.save(any())).thenReturn(defaultRoleCreateDTO());
        assertThrows(RuntimeException.class, () -> roleService.save(defaultRoleCreateDTO()));
    }

    private IcesiRole defaultRoleCreate(){
        return IcesiRole.builder()
                .name("Admin")
                .description("Admin")
                .build();
    }

    private RoleCreateDTO defaultRoleCreateDTO(){
        return RoleCreateDTO.builder()
                .name("Admin")
                .description("Admin")
                .build();
    }
}
