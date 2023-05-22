package com.example.demo.API;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.DTO.ResponseIcesiRoleDTO;

@RequestMapping("/roles")
public interface IcesiRoleAPI {

    public ResponseIcesiRoleDTO add(@Valid @RequestBody String creatorRole, IcesiRoleCreateDTO icesiRoleCreateDTO);
}
