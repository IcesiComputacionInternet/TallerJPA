package co.com.icesi.icesiAccountSystem.controller;

import co.com.icesi.icesiAccountSystem.api.RoleAPI;
import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import co.com.icesi.icesiAccountSystem.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
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
    public RoleDTO createRole(RoleDTO roleDTO) {
        return roleService.saveRole(roleDTO);
    }
}
