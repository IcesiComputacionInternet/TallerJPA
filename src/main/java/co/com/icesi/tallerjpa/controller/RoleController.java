package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.dto.RoleDTO;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;

    @PostMapping("/add/role")
    public Role createUser(@RequestBody RoleDTO role){
        return roleService.save(role);
    }
}
