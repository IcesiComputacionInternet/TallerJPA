package com.icesi.TallerJPA.controller;

import com.icesi.TallerJPA.api.RoleAPI;
import com.icesi.TallerJPA.dto.request.IcesiRoleDTO;
import com.icesi.TallerJPA.dto.response.IcesiRoleResponseDTO;
import com.icesi.TallerJPA.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiRoleController implements RoleAPI {

    private final RoleService roleService;

    @Override
    public IcesiRoleResponseDTO createIcesiRole(IcesiRoleDTO role) {
        return roleService.save(role);
    }
}
