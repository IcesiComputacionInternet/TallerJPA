package com.edu.icesi.demojpa.api;

import com.edu.icesi.demojpa.dto.request.RoleDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


public interface RoleAPI {
    String BASE_ROLE_URL = "/roles";
    @GetMapping("/{name}")
    RoleDTO getRole(@PathVariable String roleName);
    @GetMapping("/get")
    List<RoleDTO> getAllRoles();
    @PostMapping("/create")
    RoleDTO createRole(@Valid @RequestBody RoleDTO role);
}
