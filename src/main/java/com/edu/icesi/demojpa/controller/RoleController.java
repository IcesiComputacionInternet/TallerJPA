package com.edu.icesi.demojpa.controller;

import com.edu.icesi.demojpa.api.RoleAPI;
import com.edu.icesi.demojpa.dto.RoleDTO;
import com.edu.icesi.demojpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edu.icesi.demojpa.api.RoleAPI.BASE_ROLE_URL;

@RestController
@RequestMapping(BASE_ROLE_URL)
@AllArgsConstructor
public class RoleController implements RoleAPI {
    private final RoleService roleService;
    @Override
    public RoleDTO getRole(String roleName) {
        return roleService.getRole(roleName);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Override
    public RoleDTO createRole(RoleDTO role) {
        return roleService.save(role);
    }
}
