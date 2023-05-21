package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import co.edu.icesi.tallerjpa.error.exception.IcesiException;
import co.edu.icesi.tallerjpa.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiRoleMapperImpl;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.service.IcesiRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static co.edu.icesi.tallerjpa.util.DTOBuilder.defaultIcesiRoleCreateDTO;
import static co.edu.icesi.tallerjpa.util.ModelBuilder.defaultIcesiRole;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class IcesiRoleServiceTest {
    private IcesiRoleRepository icesiRoleRepository;
    private IcesiRoleMapper icesiRoleMapper;
    private IcesiRoleService icesiRoleService;

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
                .description("Role for demo")
                .name(NameIcesiRole.USER.toString())
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
                .name(NameIcesiRole.ADMIN.toString())
                .build();
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiRoleService.save(defaultIcesiRoleCreateDTO()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field name: There is already a role with the name "+icesiRole.getName(), icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is already a role with the name: " + icesiRole.getName(), exception.getMessage());
        verify(icesiRoleRepository, times(0)).save(any());
    }
}
