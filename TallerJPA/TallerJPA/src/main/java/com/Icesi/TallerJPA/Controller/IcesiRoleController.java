package com.Icesi.TallerJPA.Controller;

import com.Icesi.TallerJPA.api.RoleAPI;
import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IcesiRoleController implements RoleAPI {
    private final IcesiRoleService roleService;
    @Override
    public IcesiRoleDTO createIcesiRole(IcesiRoleDTO roleDTO) {
        return roleService.save(roleDTO);
    }
}
