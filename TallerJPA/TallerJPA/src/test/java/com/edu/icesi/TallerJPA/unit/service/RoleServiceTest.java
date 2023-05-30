package com.edu.icesi.TallerJPA.unit.service;

import com.edu.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.edu.icesi.TallerJPA.mapper.RoleMapper;
import com.edu.icesi.TallerJPA.mapper.RoleMapperImpl;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.service.RoleService;
import com.edu.icesi.TallerJPA.unit.CustomJwtAuthenticationToken;
import com.edu.icesi.TallerJPA.unit.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;
import java.util.UUID;

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
        setJwtMock();
    }

    private void setJwtMock() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("scope")).thenReturn("ADMIN");
        when(jwt.getClaimAsString("userId")).thenReturn(UUID.randomUUID().toString());
        JwtAuthenticationToken jwtAuthenticationToken = new CustomJwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
    }

    @Test
    public void testCreateRole(){
        IcesiRoleDTO icesiRole = roleService.save(createRoleDTO());
        IcesiRole icesiRole1 = IcesiRole.builder()
                .name("Student")
                .description("Is a student at Icesi")
                .build();
        verify(roleRepository, times(1)).save(argThat(new IcesiRoleMatcher(icesiRole1)));
    }

    @Test
    public void testCreateRoleWithAlreadyEXISTS(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(createRole()));
        IcesiRoleDTO role = createRoleDTO();

        try {
            roleService.save(role);
            fail();

        }catch (RuntimeException exception){
            String messageOfException = exception.getMessage();
            assertEquals("ROLE ALREADY EXISTS", messageOfException);
        }
    }

    public IcesiRoleDTO createRoleDTO(){
        return IcesiRoleDTO.builder()
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
