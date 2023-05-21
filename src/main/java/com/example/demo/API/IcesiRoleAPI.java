package com.example.demo.API;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.DTO.ResponseIcesiRoleDTO;

public interface IcesiRoleAPI {
    String BASE_URL = "/roles";

    @PostMapping("/add")
    public ResponseIcesiRoleDTO add(@Valid @RequestBody IcesiRoleCreateDTO icesiRoleCreateDTO);
}
