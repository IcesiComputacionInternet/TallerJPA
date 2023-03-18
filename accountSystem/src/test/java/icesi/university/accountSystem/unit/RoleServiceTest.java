package icesi.university.accountSystem.unit;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.mapper.IcesiRoleMapper;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import icesi.university.accountSystem.services.RoleService;
import icesi.university.accountSystem.unit.matcher.RoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RoleServiceTest {
    private RoleService roleService;
    private IcesiRoleRepository icesiRoleRepository;
    private IcesiRoleMapper icesiRoleMapper;

    @BeforeEach
    public void init(){
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiRoleMapper = spy(IcesiRoleMapper.class);
        roleService = new RoleService(icesiRoleRepository, icesiRoleMapper);
    }

    @Test
    public void saveRole(){
        when(icesiRoleRepository.save(any())).thenReturn(createDefaultIcesiRole());
        when(icesiRoleMapper.fromIcesiRoleDTO(any())).thenReturn(createDefaultIcesiRole());

        roleService.save(createDefaultIcesiRoleDTO());

        verify(icesiRoleRepository, times(1)).save(any());
        verify(icesiRoleMapper, times(1)).fromIcesiRoleDTO(any());
        verify(icesiRoleRepository, times(1)).save(argThat(new RoleMatcher(createDefaultIcesiRole())));

    }

    @Test
    public void saveRoleWithNameExist(){
        when(icesiRoleRepository.save(any())).thenReturn(createDefaultIcesiRole());
        assertThrows(RuntimeException.class,()-> roleService.save(createDefaultIcesiRoleDTO()));
    }

    private IcesiRole createDefaultIcesiRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("Colaborador")
                .description("Colaborador de la icesi")
                .build();
    }
    private IcesiRoleDTO createDefaultIcesiRoleDTO(){
        return IcesiRoleDTO.builder()
                .name("Colaborador")
                .description("Colaborador de la icesi")
                .build();
    }
}
