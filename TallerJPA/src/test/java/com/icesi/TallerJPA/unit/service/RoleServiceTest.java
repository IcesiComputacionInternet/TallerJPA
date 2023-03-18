package com.icesi.TallerJPA.unit.service;

import com.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.icesi.TallerJPA.mapper.RoleMapper;
import com.icesi.TallerJPA.mapper.RoleMapperImpl;
import com.icesi.TallerJPA.model.IcesiRole;
import com.icesi.TallerJPA.repository.RoleRepository;
import com.icesi.TallerJPA.service.RoleService;
import com.icesi.TallerJPA.unit.matcher.RoleMatcher;
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
    public void setup() {
        roleRepository = mock(RoleRepository.class);
        roleMapper = spy(RoleMapperImpl.class);
        roleService = new RoleService(roleMapper, roleRepository);
    }

    private IcesiRoleDTO defaultRoleDTO() {
        return IcesiRoleDTO.builder()
                .name("Admin")
                .description("Admin")
                .build();
    }

    private IcesiRole defaultRole() {
        return IcesiRole.builder()
                .name("Admin")
                .description("Admin")
                .build();
    }


    @Test
    public void testSaveRole() {

        roleService.save(defaultRoleDTO());
        IcesiRole icesiRole = defaultRole();

        verify(roleRepository, times(1)).save(argThat(new RoleMatcher(icesiRole)));
        verify(roleMapper, times(1)).fromIcesiRoleDTO(defaultRoleDTO());
    }

    @Test
    public void testSaveRoleWhenNameAlreadyExists() {
        when(roleRepository.existsByName(any())).thenReturn(true);
        try {
            roleService.save(defaultRoleDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Name must be unique", message);
        }
    }






}
