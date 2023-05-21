package com.edu.icesi.demojpa.api;

import com.edu.icesi.demojpa.dto.request.RoleDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface RoleAPI {
    String BASE_ROLE_URL = "/roles";
    @GetMapping("/{name}")
    RoleDTO getRole(@PathVariable String roleName);
    @GetMapping
    List<RoleDTO> getAllRoles();
    @PostMapping
    RoleDTO createRole(@RequestBody RoleDTO role);
}
