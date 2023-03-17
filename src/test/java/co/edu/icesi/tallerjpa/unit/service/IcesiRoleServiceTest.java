package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiRoleMapperImpl;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.service.IcesiRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class IcesiRoleServiceTest {
    private IcesiRoleRepository icesiRoleRepository;
    private IcesiRoleMapper icesiRoleMapper;
    private IcesiRoleService icesiRoleService;

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name("Admin")
                .build();
    }

    private IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Manage the system")
                .name("Admin")
                .build();
    }

    @BeforeEach
    private void init(){
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiRoleMapper = spy(IcesiRoleMapperImpl.class);
        icesiRoleService = new IcesiRoleService(icesiRoleRepository, icesiRoleMapper);
    }

    @Test
    public void testCreateIcesiRole(){
        IcesiRoleCreateDTO icesiRoleCreateDTO = defaultIcesiRoleCreateDTO();
        when(icesiRoleRepository.findByName(icesiRoleCreateDTO.getName())).thenReturn(Optional.ofNullable(null));
        IcesiRole icesiRole1 = IcesiRole.builder()
                .description("Manage the system")
                .name("Admin")
                .build();
        icesiRoleService.save(icesiRoleCreateDTO);
        verify(icesiRoleRepository, times(1)).save(argThat(new IcesiRoleMatcher(icesiRole1)));
    }

    @Test
    public void testCreateIcesiRoleWithTheSameName(){
        IcesiRole icesiRole = defaultIcesiRole();
        when(icesiRoleRepository.findByName(icesiRole.getName())).thenReturn(Optional.ofNullable(icesiRole));
        IcesiRole icesiRole1 = IcesiRole.builder()
                .description("Manage the system")
                .name("Admin")
                .build();
        Exception exception = assertThrows(RuntimeException.class, () -> icesiRoleService.save(defaultIcesiRoleCreateDTO()));
        assertEquals("There is already a role with the name: " + icesiRole.getName(), exception.getMessage());
        verify(icesiRoleRepository, times(0)).save(any());
    }
}
