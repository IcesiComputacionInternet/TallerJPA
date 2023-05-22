package co.com.icesi.TallerJPA.unit.service.service;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapperImpl;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.service.IcesiRoleService;
import co.com.icesi.TallerJPA.unit.service.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class IcesiRoleServiceTest {
    private IcesiRoleService roleService;
    private IcesiRoleRepository roleRepository;
    private IcesiRoleMapper roleMapper;

    @BeforeEach
    public void init(){
        roleRepository = mock(IcesiRoleRepository.class);
        roleMapper = spy(IcesiRoleMapperImpl.class);
        roleService = new IcesiRoleService(roleRepository, roleMapper);
    }

    @Test
    public void testCreateRole(){
        roleService.save(defaultRoleDTO());
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(defaultRole())));
        verify(roleMapper,times(1)).fromIcesiRoleDTO(any());
    }

    @Test
    public void testCreateAnExistingRole(){
        when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultRole()));
        try{
            roleService.save(defaultRoleDTO());
        }catch (RuntimeException e){
            String messageException = e.getMessage();
            assertEquals("Role name already exists in the database: "+defaultRole().getName(), messageException);
        }
    }

    private IcesiRoleCreateDTO defaultRoleDTO() {
        return IcesiRoleCreateDTO.builder()
                .name("prueba")
                .description("prueba de la creación de un role")
                .build();
    }

    private IcesiRole defaultRole() {
        return IcesiRole.builder()
                .name("prueba")
                .description("prueba de la creación de un role")
                .build();
    }
}

