package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.RoleCreateDTO;
import co.edu.icesi.demo.error.exception.IcesiException;
import co.edu.icesi.demo.mapper.RoleMapper;
import co.edu.icesi.demo.mapper.RoleMapperImpl;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.repository.RoleRepository;
import co.edu.icesi.demo.service.RoleService;
import co.edu.icesi.demo.unit.matcher.IcesiRoleMatcher;
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
    private void init(){
        roleRepository=mock(RoleRepository.class);
        roleMapper=spy(RoleMapperImpl.class);

        roleService=new RoleService(roleRepository,roleMapper);

    }

    @Test
    public void testCreateRole(){
        roleService.save(defaultRoleCreateDTO());
        IcesiRole icesiRole= defaultIcesiRole();

        verify(roleMapper,times(1)).fromIcesiRoleDTO(defaultRoleCreateDTO());
        verify(roleRepository,times(1)).save(argThat(new IcesiRoleMatcher(icesiRole)));
        verify(roleMapper,times(1)).fromIcesiRole(any());
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        try{
            roleService.save(defaultRoleCreateDTO());
            fail();
        }catch(IcesiException exception){
            String message= exception.getMessage();
            assertEquals("Role name already exists",message);

            verify(roleMapper,never()).fromIcesiRoleDTO(any());
            verify(roleRepository,never()).save(any());
            verify(roleMapper,never()).fromIcesiRole(any());
        }
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("administrator")
                .description("is an administrator")
                .roleId(UUID.randomUUID())
                .build();
    }

    private RoleCreateDTO defaultRoleCreateDTO(){
        return RoleCreateDTO.builder()
                .name("administrator")
                .description("is an administrator")
                .build();
    }



}
