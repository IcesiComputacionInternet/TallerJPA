package com.example.jpa.controller;

import com.example.jpa.api.RoleAPI;
import com.example.jpa.dto.RoleDTO;
import com.example.jpa.model.IcesiRole;
import com.example.jpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(RoleAPI.BASE_ROLE_URL)
public class IcesiRoleController implements RoleAPI {

    private RoleService roleService;

    @Override
    public IcesiRole createRole(@RequestBody RoleDTO dto){
        return roleService.save(dto);
    }

    @Override
    public List<RoleDTO> getRoles(){
        return roleService.getRoles();
    }

    @Override
    public RoleDTO getRole(String roleId){
        return roleService.getRole(roleId);
    }
}
