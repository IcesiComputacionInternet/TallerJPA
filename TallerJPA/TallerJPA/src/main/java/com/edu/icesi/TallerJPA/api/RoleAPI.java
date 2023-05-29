package com.edu.icesi.TallerJPA.api;

import com.edu.icesi.TallerJPA.dto.IcesiRoleDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface RoleAPI {

    String BASE_ROLE_URL = "/roles";

    @GetMapping("/{name}/")
    IcesiRoleDTO getRoleByName(@PathVariable String name);

    List<IcesiRoleDTO> getAllRoles();

    @PostMapping("/create/")
    IcesiRoleDTO addRole(@Valid @RequestBody IcesiRoleDTO roleCreateDTO);
}
