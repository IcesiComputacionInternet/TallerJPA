package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.RoleAPI;
import co.edu.icesi.demo.dto.RoleCreateDTO;
import co.edu.icesi.demo.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.edu.icesi.demo.api.RoleAPI.BASE_ROLE_URL;


@RestController(BASE_ROLE_URL)
@AllArgsConstructor
public class RoleController  implements RoleAPI {

    private final RoleService roleService;
    @Override
    public RoleCreateDTO getRole(String roleName) {
        return null;
    }

    @Override
    public List<RoleCreateDTO> getAllRoles() {
        return null;
    }

    @Override
    public RoleCreateDTO addRole(RoleCreateDTO roleCreateDTO) {
        return null;
    }
}
