package com.Icesi.TallerJPA.unit.service;

import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.mapper.IcesiRoleMapper;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.mapper.IcesiRoleMapperImpl;
import com.Icesi.TallerJPA.repository.IcesiRoleRepository;
import com.Icesi.TallerJPA.service.IcesiRoleService;
import com.Icesi.TallerJPA.unit.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

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
         roleService.save(icesiRoleDTO());
        IcesiRole icesRole1 = IcesiRole.builder()
                .name("Teacher")
                .description("Is a teacher the university Icesi")
                .build();
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(icesRole1)));
    }

    @Test
    public void testCreateRoleWithExistingName(){
        when(roleRepository.findExistName(any())).thenReturn(true);
        try{
            roleService.save(icesiRoleDTO());
        }catch (RuntimeException exception){
            assertEquals("Name already exist", exception.getMessage());
        }
    }

    public IcesiRoleDTO icesiRoleDTO(){
        return IcesiRoleDTO.builder()
                .name("Teacher")
                .description("Is a teacher the university Icesi")
                .build();
    }
    public IcesiRoleDTO icesiRoleDTO2(){
        return IcesiRoleDTO.builder()
                .name("Teacher")
                .description("Is a teacher the university Icesi")
                .build();
    }

    public IcesiRole icesiRole(){
        return IcesiRole.builder()
                .name("Teacher")
                .description("Is a teacher the university Icesi")
                .build();
    }

    public IcesiRole icesiRole2(){
        return IcesiRole.builder()
                .name("Teacher")
                .description("Is a teacher the university Icesi")
                .build();
    }
}
