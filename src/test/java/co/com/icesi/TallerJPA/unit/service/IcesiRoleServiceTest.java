package co.com.icesi.TallerJPA.unit.service;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapperImpl;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import co.com.icesi.TallerJPA.unit.matcher.IcesiRoleMatcher;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.service.IcesiRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//TODO: ACTUALIZAR CON EL NUEVO METODO
public class IcesiRoleServiceTest {
    private IcesiRoleService roleService;
    private IcesiRoleRepository roleRepository;

    private IcesiRoleMapper roleMapper;

    private IcesiUserRepository userRepository;

    @BeforeEach
    public void init(){
        roleRepository = mock(IcesiRoleRepository.class);
        roleMapper = spy(IcesiRoleMapperImpl.class);
        userRepository = mock(IcesiUserRepository.class);
        roleService = new IcesiRoleService(roleRepository, roleMapper,userRepository);
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
