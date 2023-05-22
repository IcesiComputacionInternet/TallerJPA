package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /*@PostMapping("/roles/add")
    public IcesiRole createIcesiRole(@RequestBody RoleCreateDTO role){
        return roleService.save(role);
    }*/


    
}
