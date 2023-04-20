package com.example.TallerJPA.controller;

import com.example.TallerJPA.api.RoleAPI;
import com.example.TallerJPA.dto.RoleDTO;
import com.example.TallerJPA.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.TallerJPA.api.RoleAPI.BASE_ROLE_URL;

@AllArgsConstructor
@RestController(BASE_ROLE_URL)
public class RoleController implements RoleAPI {
    private RoleService roleService;

    @Override
    public RoleDTO createRole(@RequestBody RoleDTO role){
        return roleService.save(role);
    }
}
