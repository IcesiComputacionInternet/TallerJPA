package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.servicio.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public IcesiRole createIcesiRole(@RequestBody RoleCreateDTO role){
        return roleService.save(role);
    }

    @GetMapping("/roles/{id}")
    public IcesiRole returnRole(@PathVariable String id){
        return roleService.findById(UUID.fromString(id)).orElseThrow();
    }
}
