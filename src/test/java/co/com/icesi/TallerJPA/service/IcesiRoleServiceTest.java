package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapperImpl;
import co.com.icesi.TallerJPA.matcher.IcesiRoleMatcher;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    public void testSave(){
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());
        roleService.save(defaultDTO());
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(defaultRole())));
        verify(roleMapper, times(1)).fromRoleDTO(any());
    }

    @Test
    public void testCreateARoleWhenNameAlreadyExist(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultRole()));
       assertThrows(RuntimeException.class, () -> roleService.save(defaultDTO()));

    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .build();
    }

    private IcesiRoleDTO defaultDTO(){
        return   IcesiRoleDTO.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .build();
    }


}
