package co.com.icesi.demojpa.unit.service;
import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.mapper.RoleMapperImpl;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.servicio.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
public class RoleServiceTest {

    private RoleMapper roleMapper;

    private RoleService roleService;

    private RoleRepository roleRepository;

    @BeforeEach
    private void init(){
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleRepository,roleMapper);
    }

    //Se crea rol sin problema :D
    @Test
    public void testCreateRole(){
        roleService.save(defaultRoleDTO());
        IcesiRole icesiRole = defaultRole();
        verify(roleRepository,times(1)).save(argThat(new IcesiRoleMatcher(icesiRole)));
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultRole()));
        try{
            roleService.save(defaultRoleDTO());
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Ya existe un rol con este nombre",message);
        }
    }

    private RoleCreateDTO defaultRoleDTO(){
        return RoleCreateDTO.builder()
                .description("Rol de prueba")
                .name("nombre de rol de prueba")
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("nombre de rol de prueba")
                .description("Rol de prueba")
                .build();
    }
}
