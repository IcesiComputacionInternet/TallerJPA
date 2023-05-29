package com.edu.icesi.TallerJPA.controller;

import com.edu.icesi.TallerJPA.api.RoleAPI;
import com.edu.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.edu.icesi.TallerJPA.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edu.icesi.TallerJPA.api.RoleAPI.BASE_ROLE_URL;

@RequestMapping(BASE_ROLE_URL)
@RestController
@AllArgsConstructor
public class RoleController implements RoleAPI {

    private final RoleService roleService;

    @Override
    public IcesiRoleDTO getRoleByName(String name) {
        return null;
    }

    @Override
    public List<IcesiRoleDTO> getAllRoles() {
        return null;
    }

    @Override
    public IcesiRoleDTO addRole(IcesiRoleDTO roleCreateDTO) {
        return roleService.save(roleCreateDTO);
    }
}
