package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.RoleAPI;
import co.edu.icesi.demo.dto.RoleDTO;
import co.edu.icesi.demo.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.edu.icesi.demo.api.RoleAPI.BASE_ROLE_URL;


@RestController
@RequestMapping(BASE_ROLE_URL)
@AllArgsConstructor
@CrossOrigin
public class RoleController  implements RoleAPI {

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
    public RoleDTO addRole(RoleDTO roleDTO) {

        return roleService.save(roleDTO);
    }
}
