package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.IcesiRoleAPI;
import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiRoleAPI.BASE_ROLE_URL;

@RestController
@AllArgsConstructor
public class RoleController implements IcesiRoleAPI {
    private final RoleService roleService;

    @Override
    public RoleCreateDTO createIcesiRole(RoleCreateDTO role) {
        return roleService.save(role);
    }

    @Override
    public RoleCreateDTO getRoleByName(String roleName) {
        return roleService.getRoleByName(roleName);
    }

    @Override
    public List<IcesiRole> getAllRoles() {
        return roleService.getAllRoles();
    }


}
