package com.example.demo.api;

import com.example.demo.DTO.IcesiRoleDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/roles")
public interface IcesiRoleApi {
    @PostMapping("/add/role")
    public IcesiRoleDTO createRole(@RequestBody IcesiRoleDTO role);
}