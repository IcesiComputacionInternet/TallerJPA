package co.com.icesi.tallerjpa.unit.service;


import co.com.icesi.tallerjpa.error.exception.IcesiException;
import co.com.icesi.tallerjpa.mapper.RoleMapper;
import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.mapper.RoleMapperImpl;
import co.com.icesi.tallerjpa.model.IcesiRole;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.service.RoleService;
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
    public void init(){
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleRepository, roleMapper);
    }

    /*@Test
    public void testCreateRole(){
        roleService.save(defaultRoleCreateDTO());
        IcesiRole icesiRole1 = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("no description")
                .name("Student")
                .build();
        verify(roleRepository,times( 1)).save(argThat(new IcesiRoleMatcher(icesiRole1)));
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        try {
            roleService.save(defaultRoleCreateDTO());
            fail();
        } catch (IcesiException exception){
            String message = exception.getMessage();
            System.out.println(message);
            assertEquals("Role name is not unique", message);
        }
    }

    private RoleCreateDTO defaultRoleCreateDTO() {
        return RoleCreateDTO.builder()
                .description("no description")
                .name("Student")
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("no description")
                .name("Student")
                .build();
    }*/
}
