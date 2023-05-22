package com.example.jpa.api;

import com.example.jpa.dto.RoleDTO;
import com.example.jpa.model.IcesiRole;
import com.sun.xml.bind.v2.TODO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(RoleAPI.BASE_ROLE_URL)
public interface RoleAPI {

    String BASE_ROLE_URL = "/roles";

    @PostMapping("/createRole")
    IcesiRole createRole(@RequestBody RoleDTO dto);

    @GetMapping("/{roleId}")
    RoleDTO getRole(@PathVariable("roleId") String roleId);

    @GetMapping("/all")
    List<RoleDTO> getRoles();
}
