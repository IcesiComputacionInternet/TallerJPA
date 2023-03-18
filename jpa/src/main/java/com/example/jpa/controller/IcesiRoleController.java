package com.example.jpa.controller;

import com.example.jpa.dto.RoleDTO;
import com.example.jpa.model.IcesiRole;
import com.example.jpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
public class IcesiRoleController {

    private RoleService roleService;

    @PostMapping
    public IcesiRole createRole(@RequestBody RoleDTO dto){
        return roleService.save(dto);
    }

    @GetMapping
    public List<RoleDTO> getRoles(){
        return roleService.getRoles();
    }
}
