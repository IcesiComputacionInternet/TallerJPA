package com.example.tallerjpa.api;

import com.example.tallerjpa.dto.RoleDTO;
import com.example.tallerjpa.model.IcesiRole;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Validated
@RequestMapping(RoleAPI.BASE_ROLE_URL)
public interface RoleAPI {

    String BASE_ROLE_URL = "/roles";

    @PostMapping
    IcesiRole createRole(@RequestBody @Valid RoleDTO roleDTO);







}
