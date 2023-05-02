package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.api.RoleAPI;
import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.com.icesi.tallerjpa.api.RoleAPI.ROLE_URL;
@RestController
@RequestMapping(ROLE_URL)
@AllArgsConstructor
public class RoleController implements RoleAPI {
    private final RoleService roleService;
    @Override
    public RoleCreateDTO getRole(String roleName){
        return roleService.getRole(roleName);
    }

    @GetMapping
    public List<RoleCreateDTO> getAllRoles(){
        return roleService.getAllRoles();
    }

    @Override
    public RoleCreateDTO addRole(@RequestBody RoleCreateDTO roleCreateDTO){
        return roleService.save(roleCreateDTO);
    }

}
