package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.API.IcesiRoleAPI;
import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.service.IcesiRoleService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class IcesiRoleController implements IcesiRoleAPI{
    
    private IcesiRoleService icesiRoleService;

    @Override
    public IcesiRoleCreateDTO add(@RequestBody IcesiRoleCreateDTO role) {
        return icesiRoleService.create(role);
    }
}
