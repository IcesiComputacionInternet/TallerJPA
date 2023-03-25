package com.edu.icesi.TallerJPA.controller;

import com.edu.icesi.TallerJPA.api.RoleAPI;
import com.edu.icesi.TallerJPA.dto.RoleCreateDTO;
import com.edu.icesi.TallerJPA.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edu.icesi.TallerJPA.api.RoleAPI.BASE_ROLE_URL;

@RestController(BASE_ROLE_URL)
@AllArgsConstructor
public class RoleController implements RoleAPI {

    private final RoleService roleService;

    @Override
    public RoleCreateDTO getRoleByName(String name) {
        return null;
    }

    @Override
    public List<RoleCreateDTO> getAllRoles() {
        return null;
    }

    @Override
    public RoleCreateDTO addRole(RoleCreateDTO roleCreateDTO) {
        return roleService.save(roleCreateDTO);
    }
}
