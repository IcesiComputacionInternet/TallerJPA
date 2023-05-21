package com.example.TallerJPA.controller;

import com.example.TallerJPA.api.RoleAPI;
import com.example.TallerJPA.dto.RoleDTO;
import com.example.TallerJPA.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.TallerJPA.api.RoleAPI.BASE_ROLE_URL;

@AllArgsConstructor
@RestController
public class RoleController implements RoleAPI {
    private RoleService roleService;

    @Override
    public RoleDTO createRole(@RequestBody @Valid RoleDTO role){
        return roleService.save(role);
    }
}
