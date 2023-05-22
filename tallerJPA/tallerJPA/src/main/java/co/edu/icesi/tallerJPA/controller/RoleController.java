package co.edu.icesi.tallerJPA.controller;


import co.edu.icesi.tallerJPA.dto.RoleDTO;
import co.edu.icesi.tallerJPA.model.Role;
import co.edu.icesi.tallerJPA.service.RoleService;
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