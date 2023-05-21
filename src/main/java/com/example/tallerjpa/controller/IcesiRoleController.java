package com.example.tallerjpa.controller;

import com.example.tallerjpa.api.RoleAPI;
import com.example.tallerjpa.dto.RoleDTO;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
public class IcesiRoleController implements RoleAPI {

    private final RoleService roleService;

    @PostMapping
    public IcesiRole createRole(@RequestBody @Valid RoleDTO roleDTO){return roleService.saveRole(roleDTO);}
}
