package co.edu.icesi.tallerjpa.runableartefact.unit.service.code;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.DataAlreadyExist;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiRoleService;
import co.edu.icesi.tallerjpa.runableartefact.unit.service.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class IcesiRoleServiceTest {

    private IcesiRoleService icesiRoleService;
    private IcesiRoleRepository icesiRoleRepository;
    private IcesiRoleMapper icesiRoleMapper;

    @BeforeEach
    public void init(){
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiRoleMapper = spy(IcesiRoleMapper.class);
        icesiRoleService = new IcesiRoleService(icesiRoleRepository, icesiRoleMapper);
    }

    @Test
    public void saveNewRoleTest(){
        when(icesiRoleRepository.save(any())).thenReturn(createDefaultIcesiRole());
        when(icesiRoleMapper.toIcesiRole(any())).thenReturn(createDefaultIcesiRole());
        when(icesiRoleRepository.existsByName(any())).thenReturn(false);

        icesiRoleService.saveNewRole(createDefaultIcesiRoleDTO());

        verify(icesiRoleRepository, times(1)).save(any());
        verify(icesiRoleMapper, times(1)).toIcesiRole(any());
        verify(icesiRoleRepository, times(1)).save(argThat(new IcesiRoleMatcher(createDefaultIcesiRole())));
    }

    @Test
    public void saveNewRoleWhenNameAlreadyExistsTest(){
        when(icesiRoleRepository.existsByName(any())).thenReturn(true);
        when(icesiRoleMapper.toIcesiRole(any())).thenReturn(createDefaultIcesiRole());
        when(icesiRoleRepository.save(any())).thenReturn(createDefaultIcesiRole());

        try {
            icesiRoleService.saveNewRole(createDefaultIcesiRoleDTO());
        }catch (DataAlreadyExist e) {
            assertEquals("Role name already exists", e.getMessage());
        }

    }

    private IcesiRole createDefaultIcesiRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("ADMIN")
                .description("Administrador del sistema")
                .build();
    }
    private IcesiRoleDTO createDefaultIcesiRoleDTO(){
        return IcesiRoleDTO.builder()
                .name("ADMIN")
                .description("Administrador del sistema")
                .build();
    }
}
