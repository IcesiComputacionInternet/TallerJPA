package com.Icesi.TallerJPA.api;

import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.model.IcesiRole;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(RoleApi.ROLE_BASE_URL)
public interface RoleApi {
    String ROLE_BASE_URL = "/roles";
    @PostMapping
    IcesiRoleDTO add(@RequestBody IcesiRoleDTO roleDTO);
}
