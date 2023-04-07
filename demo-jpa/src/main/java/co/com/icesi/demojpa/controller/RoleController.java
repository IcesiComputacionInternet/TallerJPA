package co.com.icesi.demojpa.controller;


import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/roles")
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;

    //Debe devolver un DTO en vez de un normal
    @PostMapping("/add")
    public IcesiRole addRole(@RequestBody RoleCreateDTO role){
        return roleService.save(role);
    }

    @GetMapping("/{roleId}")
    public IcesiRole getRoleById(String roleId){
        return null;
    }
}
