package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.servicio.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleRepository roleRepository) {
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
