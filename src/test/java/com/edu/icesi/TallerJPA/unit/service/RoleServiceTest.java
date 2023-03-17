package com.edu.icesi.TallerJPA.unit.service;

import com.edu.icesi.TallerJPA.dto.RoleCreateDTO;
import com.edu.icesi.TallerJPA.mapper.RoleMapper;
import com.edu.icesi.TallerJPA.mapper.RoleMapperImpl;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    public RoleCreateDTO createRoleDTO(){
        return RoleCreateDTO.builder()
                .name("Student")
                .description("Is a student at Icesi")
                .build();
    }
}
