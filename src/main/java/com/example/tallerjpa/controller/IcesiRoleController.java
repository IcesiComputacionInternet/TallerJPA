package com.example.tallerjpa.controller;

import com.example.tallerjpa.dto.RoleDTO;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiRoleController {

    private final RoleService roleService;

    @PostMapping("/role")
    public IcesiRole createRole(@RequestBody RoleDTO roleDTO) {return roleService.saveRole(roleDTO);}
}
