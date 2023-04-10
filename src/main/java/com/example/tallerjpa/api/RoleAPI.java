package com.example.tallerjpa.api;

import com.example.tallerjpa.dto.RoleDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RoleAPI {

    String BASE_ROLE_URL = "/roles";

    @GetMapping("{roleId}")
    RoleDTO getRole(@PathVariable RoleDTO roleId);

    List<RoleDTO> getAllRoles();

    RoleDTO addRole (RoleDTO roleDTO);




}
