package com.example.demo.API;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.DTO.IcesiRoleCreateDTO;

public interface IcesiRoleAPI {
    String BASE_URL = "/roles";

    @PostMapping("/add")
    IcesiRoleCreateDTO add(@RequestBody IcesiRoleCreateDTO role);
}
