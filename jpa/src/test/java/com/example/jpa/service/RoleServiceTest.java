package com.example.jpa.service;

import com.example.jpa.dto.RoleDTO;
import com.example.jpa.mapper.RoleMapper;
import com.example.jpa.mapper.RoleMapperImpl;
import com.example.jpa.matcher.RoleMatcher;
import com.example.jpa.model.IcesiRole;
import com.example.jpa.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    private RoleService roleService;
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    private final String ROLE_USER = "Colaborador";

    @BeforeEach
    private void init(){
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleRepository, roleMapper);
    }

    @Test
    public void testSaveRole(){
        when(roleRepository.getByName(any())).thenReturn(Optional.empty());
        roleService.save(defaultRoleDTO());
        verify(roleRepository, times(1)).save(argThat(new RoleMatcher(defaultRole())));
        verify(roleMapper, times(1)).fromRoleDTO(any());
    }

    @Test
    public void testSaveRoleWhenRoleNameAlreadyExists(){
        when(roleRepository.getByName(any())).thenReturn(Optional.of(defaultRole()));
        try {
            roleService.save(defaultRoleDTO());
            fail();
        }catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Role name already exists", message);
        }
    }

    @Test
    public void testGetRoles(){
        roleService.getRoles();
        verify(roleRepository, times(1)).findAll();
    }

    private RoleDTO defaultRoleDTO() {
        return RoleDTO.builder()
                .name(ROLE_USER)
                .description("User role test")
                .build();
    }

    private IcesiRole defaultRole() {
        return IcesiRole.builder()
                .name(ROLE_USER)
                .description("User role test")
                .build();
    }

}
