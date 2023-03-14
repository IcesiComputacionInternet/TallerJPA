package com.icesi.TallerJPA.controller;

import com.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.icesi.TallerJPA.model.IcesiRole;
import com.icesi.TallerJPA.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiRoleController {

    private final RoleService roleService;

    @PostMapping("/role")
    public IcesiRole createIcesiRole(@RequestBody IcesiRoleDTO role){
        return roleService.save(role);
    }
}
