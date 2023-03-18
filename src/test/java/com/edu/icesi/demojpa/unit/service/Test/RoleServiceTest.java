package com.edu.icesi.demojpa.unit.service.Test;

import com.edu.icesi.demojpa.dto.RoleCreateDTO;
import com.edu.icesi.demojpa.mapper.RoleMapper;
import com.edu.icesi.demojpa.mapper.RoleMapperImpl;
import com.edu.icesi.demojpa.mapper.UserMapperImpl;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.repository.RoleRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import com.edu.icesi.demojpa.service.RoleService;
import com.edu.icesi.demojpa.service.UserService;
import com.edu.icesi.demojpa.unit.service.Matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        IcesiRole icesiRole = roleService.save(defaultRoleDTO());
        IcesiRole icesiRoleToCompare = defaultIcesiRole();
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(icesiRoleToCompare)));
    }

    @Test
    public void testCreateRoleWhenItExists(){
        when(roleRepository.findRoleByName(any())).thenReturn(Optional.ofNullable(defaultIcesiRole()));
        try{
            roleService.save(defaultRoleDTO());
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The name is already in use", message);
        }
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("Student")
                .description("Loreno Insomnio, nunca supe como se dice")
                .build();
    }

    private RoleCreateDTO defaultRoleDTO(){
        return RoleCreateDTO.builder()
                .name("Student")
                .description("Loreno Insomnio, nunca supe como se dice")
                .build();
    }
}
