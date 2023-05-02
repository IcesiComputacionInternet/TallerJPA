/**package com.example.TallerJPA.unit.service;

import com.example.TallerJPA.dto.RoleDTO;
import com.example.TallerJPA.mapper.RoleMapper;
import com.example.TallerJPA.mapper.RoleMapperImpl;
import com.example.TallerJPA.model.IcesiRole;
import com.example.TallerJPA.repository.RoleRepository;
import com.example.TallerJPA.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


public class RoleServiceTest {
    private RoleService roleService;
    private RoleMapper roleMapper;
    private RoleRepository roleRepository;

    @BeforeEach
    public void init() {
        roleMapper = spy(RoleMapperImpl.class);
        roleRepository = mock(RoleRepository.class);
        roleService = new RoleService(roleMapper, roleRepository);
    }

    @Test
    public void testCreateRole() {
        roleService.save(defaultRoleCreateDTO());
        IcesiRole role = IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("testRole")
                .description("testDescription")
                .build();
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(role)));
    }

    @Test
    public void testCreateRoleWhenNameAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        assertThrows(RuntimeException.class,() -> roleService.save(defaultRoleCreateDTO()));
    }
    @Test
    public void testRoleDoesNotExist(){
        when(roleRepository.findById(any())).thenReturn(Optional.empty());
        assertEquals(Optional.empty(),roleService.findRoleByName("testRole"));
    }

    private RoleDTO defaultRoleCreateDTO() {
        return RoleDTO.builder()
                .name("testRole")
                .description("testDescription")
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("testRole")
                .description("testDescription")
                .build();
    }
}
 **/