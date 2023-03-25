package com.edu.icesi.TallerJPA.api;

import com.edu.icesi.TallerJPA.dto.RoleCreateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RoleAPI {

    String BASE_ROLE_URL = "/roles";

    @GetMapping("/{name}/")
    RoleCreateDTO getRoleByName(@PathVariable String name);

    List<RoleCreateDTO> getAllRoles();

    RoleCreateDTO addRole(@RequestBody RoleCreateDTO roleCreateDTO);
}
