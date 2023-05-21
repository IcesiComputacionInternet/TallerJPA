package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoleController {

    private IcesiRoleService icesiRoleService;

    @PostMapping("/add/role")
    public IcesiRole createUser(@RequestBody IcesiRoleDto role){
        return icesiRoleService.saveRole(role);
    }
}
