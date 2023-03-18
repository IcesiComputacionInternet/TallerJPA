package co.com.icesi.TallerJpa.unit.service;

import co.com.icesi.TallerJpa.dto.IcesiRoleCreateDTO;
import co.com.icesi.TallerJpa.exceptions.icesiRoleExceptions.RoleNameAlreadyInUseException;
import co.com.icesi.TallerJpa.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJpa.mapper.IcesiRoleMapperImpl;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import co.com.icesi.TallerJpa.service.IcesiRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
    public void testCreateRole(){
        icesiRoleService.saveRole(defaultRoleCreateDTO());
        IcesiRole icesiRole = IcesiRole.builder()
                .name("ROLE_ADMIN")
                .description("Administrador de la aplicación")
                .build();
        verify(icesiRoleRepository,times(1)).save(argThat(new IcesiRoleMatcher(icesiRole)));
    }
    @Test
    public void testGetRole(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        try{
            icesiRoleService.saveRole(defaultRoleCreateDTO());
            fail();
        }catch (RoleNameAlreadyInUseException exception){
            String message = exception.getMessage();
            assertEquals("RoleNameAlreadyInUseException: The name role is already in use", message);
        }
    }
    public IcesiRoleCreateDTO defaultRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .name("ROLE_ADMIN")
                .description("Administrador de la aplicación")
                .build();
    }
    public IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("ROLE_ADMIN")
                .description("Administrador de la aplicación")
                .build();
    }
}
