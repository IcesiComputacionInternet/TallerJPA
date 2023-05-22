package com.example.tallerjpa.unit.service.service;

import com.example.tallerjpa.dto.RoleDTO;
import com.example.tallerjpa.mapper.RoleMapper;
import com.example.tallerjpa.mapper.RoleMapperImpl;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.repository.RoleRepository;
import com.example.tallerjpa.service.RoleService;
import com.example.tallerjpa.unit.service.matcher.RoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    private RoleService roleService;
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    @BeforeEach
    public void setup(){
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleRepository, roleMapper);
    }

    @Test
    public void testSaveRole() {
        roleService.saveRole(defaultRoleDTO());
        verify(roleRepository, times(1)).save(argThat(new RoleMatcher(defaultRole())));
        verify(roleMapper, times(1)).fromRoleDTO(defaultRoleDTO());
    }

    @Test
    public void testNameRoleExists(){
        when(roleRepository.existsByName(any())).thenReturn(true);
        try {
            roleService.saveRole(defaultRoleDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Role's name must be unique", message);
        }
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("ADMIN")
                .description("Role for admin's")
                .build();
    }

    private RoleDTO defaultRoleDTO(){
        return RoleDTO.builder()
                .name("ADMIN")
                .description("Role for admin's")
                .build();
    }

}
