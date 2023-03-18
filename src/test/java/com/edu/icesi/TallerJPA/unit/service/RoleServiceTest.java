package com.edu.icesi.TallerJPA.unit.service;

import com.edu.icesi.TallerJPA.dto.RoleCreateDTO;
import com.edu.icesi.TallerJPA.mapper.RoleMapper;
import com.edu.icesi.TallerJPA.mapper.RoleMapperImpl;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.service.RoleService;
import com.edu.icesi.TallerJPA.unit.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    private RoleService roleService;

    private RoleRepository roleRepository;

    private RoleMapper roleMapper;

    @BeforeEach
    private void init(){
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleRepository, roleMapper);
    }

    @Test
    public void testCreateRole(){
        IcesiRole icesiRole = roleService.save(createRoleDTO());
        IcesiRole icesiRole1 = IcesiRole.builder()
                .name("Student")
                .description("Is a student at Icesi")
                .build();
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(icesiRole1)));
    }

    @Test
    public void testCreateRoleWithAlreadyName(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(createRole()));

        try {
            roleService.save(createRoleDTO());
            fail();

        }catch (RuntimeException exception){
            String messageOfException = exception.getMessage();
            assertEquals("Role already exists", messageOfException);
        }
    }

    public RoleCreateDTO createRoleDTO(){
        return RoleCreateDTO.builder()
                .name("Student")
                .description("Is a student at Icesi")
                .build();
    }

    public IcesiRole createRole(){
        return IcesiRole.builder()
                .name("Student")
                .description("Is a student at Icesi")
                .build();
    }
}
