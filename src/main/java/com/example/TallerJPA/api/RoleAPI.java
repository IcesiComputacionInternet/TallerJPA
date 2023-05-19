package com.example.TallerJPA.api;

import com.example.TallerJPA.dto.RoleDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(RoleAPI.BASE_ROLE_URL)
public interface RoleAPI {
    String BASE_ROLE_URL = "/roles";
    @PostMapping("/add/role")
    public RoleDTO createRole(@RequestBody RoleDTO role);
}
