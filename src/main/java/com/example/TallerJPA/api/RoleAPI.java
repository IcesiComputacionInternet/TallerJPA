package com.example.TallerJPA.api;

import com.example.TallerJPA.dto.RoleDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface RoleAPI {
    String BASE_ROLE_URL = "/roles";
    @PostMapping("/add/role")
    public RoleDTO createRole(@RequestBody RoleDTO role);
}
