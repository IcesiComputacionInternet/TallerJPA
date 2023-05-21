package co.com.icesi.TallerJpa.unit.service;

import co.com.icesi.TallerJpa.dto.IcesiRoleDTO;
import co.com.icesi.TallerJpa.error.exception.IcesiError;
import co.com.icesi.TallerJpa.error.exception.IcesiException;
import co.com.icesi.TallerJpa.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJpa.mapper.IcesiRoleMapperImpl;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import co.com.icesi.TallerJpa.service.IcesiRoleService;
import co.com.icesi.TallerJpa.unit.service.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiRoleServiceTest {
    private IcesiRoleService icesiRoleService;
    private IcesiRoleRepository icesiRoleRepository;
    private IcesiRoleMapper icesiRoleMapper;

    @BeforeEach
    public void init(){
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiRoleMapper = spy(IcesiRoleMapperImpl.class);
        icesiRoleService = new IcesiRoleService(icesiRoleRepository,icesiRoleMapper);
    }

    @Test
    @DisplayName("Create role Happy Path.")
    public void testCreateRoleHappyPath(){
        when(icesiRoleRepository.existsByName(any())).thenReturn(false);
        icesiRoleService.saveRole(defaultIcesiRoleDTO());
        verify(icesiRoleMapper,times(1)).fromRoleDto(any());
        verify(icesiRoleMapper,times(1)).fromIcesiRole(any());
        verify(icesiRoleRepository,times(1)).save(argThat(new IcesiRoleMatcher(defaultIcesiRole())));
    }

    @Test
    @DisplayName("Create role when the name is already in use.")
    public void testCreateRoleWhenNameAlreadyExists(){
        when(icesiRoleRepository.existsByName(any())).thenReturn(true);

        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiRoleService.saveRole(defaultIcesiRoleDTO()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("The name "+ defaultIcesiRoleDTO().getName() +" is already in use"
                , icesiException.getMessage());
        assertEquals("ERR_DUPLICATED", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("resource IcesiRole with field name: "+ defaultIcesiRoleDTO().getName() +", already exists"
                , icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiRoleRepository,times(1)).existsByName(any());
        verify(icesiRoleMapper,times(0)).fromRoleDto(any());
        verify(icesiRoleMapper,times(0)).fromIcesiRole(any());
        verify(icesiRoleRepository,times(0)).save(argThat(new IcesiRoleMatcher(defaultIcesiRole())));
    }

    @Test
    @DisplayName("Create a list of roles.")
    public void testCreateListOfRoles(){
        when(icesiRoleRepository.existsByName(any())).thenReturn(false);
        icesiRoleService.saveListRoles(defaultIcesiRoleDTOList());
        verify(icesiRoleMapper,times(3)).fromRoleDto(any());
        verify(icesiRoleMapper,times(3)).fromIcesiRole(any());
        defaultIcesiRoleList().stream()
                .map(IcesiRoleMatcher::new)
                .forEach(role -> verify(icesiRoleRepository, times(1)).save(argThat(role)));
    }

    private IcesiRoleDTO defaultIcesiRoleDTO(){
        return IcesiRoleDTO.builder()
                .name("ROLE_ADMIN")
                .description("Administrador de la universidad")
                .build();
    }
    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("ROLE_ADMIN")
                .description("Administrador de la universidad")
                .build();
    }
    private List<IcesiRoleDTO> defaultIcesiRoleDTOList(){
        return List.of(
                IcesiRoleDTO.builder()
                        .name("ROLE_ADMIN")
                        .description("Administrador de la universidad")
                        .build(),
                IcesiRoleDTO.builder()
                        .name("ROLE_COLABORADOR")
                        .description("Colaborador de la universidad")
                        .build(),
                IcesiRoleDTO.builder()
                        .name("ROLE_ESTUDIANTE")
                        .description("Estudiante de la universidad")
                        .build()
                );
    }
    private List<IcesiRole> defaultIcesiRoleList(){
        return List.of(
                IcesiRole.builder()
                        .name("ROLE_ADMIN")
                        .description("Administrador de la universidad")
                        .build(),
                IcesiRole.builder()
                        .name("ROLE_COLABORADOR")
                        .description("Colaborador de la universidad")
                        .build(),
                IcesiRole.builder()
                        .name("ROLE_ESTUDIANTE")
                        .description("Estudiante de la universidad")
                        .build()
        );
    }
}
