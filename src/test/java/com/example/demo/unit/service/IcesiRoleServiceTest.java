package com.example.demo.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.mapper.IcesiRoleMapper;
import com.example.demo.mapper.IcesiRoleMapperImpl;
import com.example.demo.model.IcesiRole;
import com.example.demo.repository.IcesiRoleRepository;
import com.example.demo.service.IcesiRoleService;

public class IcesiRoleServiceTest {
    private IcesiRoleService icesiRoleService;

    private IcesiRoleRepository icesiRoleRepository;

    private IcesiRoleMapper icesiRoleMapper;

    @BeforeEach
    private void init() {
        icesiRoleMapper = spy(IcesiRoleMapperImpl.class);
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiRoleService = new IcesiRoleService(icesiRoleRepository, icesiRoleMapper);
    }

    @Test
    public void testCreateIcesiRole() {
        icesiRoleService.create(defaultIcesiRoleCreateDTO());
        IcesiRole icesiRole1 = IcesiRole.builder()
            .description("This role is for cleanning personal")
            .name("Cleaning")
            .build();
        verify(icesiRoleRepository, times(1)).save(argThat(new IcesiRoleMatcher(icesiRole1)));
    }

    private IcesiRoleCreateDTO defaultIcesiRoleCreateDTO() {
        return IcesiRoleCreateDTO.builder()
        .description("This role is for cleanning personal")
        .name("Cleaning")
        .build();
    }

    @Test
    public void testCreateIcesiRoleWhenNameAlreadyExists() {
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        try {
            icesiRoleService.create(defaultIcesiRoleCreateDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("This role name is already in use", message);
        }
    }

    private IcesiRole defaultIcesiRole() {
        return IcesiRole.builder()
        .description("This role is for cleanning personal")
        .name("Cleaning")
        .build();
    }
}
