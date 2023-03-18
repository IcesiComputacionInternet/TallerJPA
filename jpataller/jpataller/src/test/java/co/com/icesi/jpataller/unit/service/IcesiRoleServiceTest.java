package co.com.icesi.jpataller.unit.service;

import co.com.icesi.jpataller.dto.IcesiRoleDTO;
import co.com.icesi.jpataller.mapper.IcesiRoleMapper;
import co.com.icesi.jpataller.mapper.IcesiRoleMapperImpl;
import co.com.icesi.jpataller.model.IcesiRole;
import co.com.icesi.jpataller.model.IcesiUser;
import co.com.icesi.jpataller.repository.IcesiRoleRepository;
import co.com.icesi.jpataller.repository.IcesiUserRepository;
import co.com.icesi.jpataller.service.IcesiRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class IcesiRoleServiceTest {

    private IcesiRoleMapper icesiRoleMapper;
    private IcesiRoleService icesiRoleService;
    private IcesiRoleRepository icesiRoleRepository;
    private IcesiUserRepository icesiUserRepository;

    private IcesiRoleDTO defaultIcesiRoleDTO(){
        return IcesiRoleDTO.builder()
                .description("Rol de prueba")
                .name("Rol 3")
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("Rol 3")
                .description("Rol de prueba")
                .users(new ArrayList<>())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Gabriel")
                .lastName("Delgado")
                .email("gabreldelgado2002@gmail.com")
                .phoneNumber("3167354")
                .password("sape")
                .role(IcesiRole.builder()
                        .name("Rol de prueba 1")
                        .description("Rol 2")
                        .build())
                .build();
    }

    @BeforeEach
    private void init(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiRoleMapper = spy(IcesiRoleMapperImpl.class);
        icesiRoleService = new IcesiRoleService(icesiRoleRepository, icesiUserRepository, icesiRoleMapper);
    }

    @Test
    public void testCreateRole() {
        icesiRoleService.createRole(defaultIcesiRoleDTO());
        IcesiRole icesiRole = defaultIcesiRole();
        verify(icesiRoleRepository, times(1)).save(argThat(new IcesiRoleMatcher(icesiRole)));
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        try{
            icesiRoleService.createRole(defaultIcesiRoleDTO());
            fail();
        }catch (RuntimeException exception){
            String exceptionMessage = exception.getMessage();
            assertEquals("Ya existe un rol con ese nombre",exceptionMessage);
        }
    }

    @Test
    public void testAddUserToRoleWhenUserDoesNotExist(){
        IcesiUser user = defaultIcesiUser();
        IcesiRole role =defaultIcesiRole();
        when(icesiUserRepository.findById(any())).thenReturn(Optional.empty());
        try{
            icesiRoleService.addUserToRole(role,user.getUserId());
            fail();
        }catch (RuntimeException exception){
            String exceptionMessage = exception.getMessage();
            assertEquals("No existe un usuario con este id",exceptionMessage);
        }
    }
}
