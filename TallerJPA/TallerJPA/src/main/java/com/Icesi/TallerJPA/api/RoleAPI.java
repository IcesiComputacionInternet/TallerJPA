package com.Icesi.TallerJPA.api;

import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(RoleAPI.ROLE_BASE_URL)
public interface RoleAPI {
    String ROLE_BASE_URL = "/roles";

    @PostMapping
    IcesiRoleDTO createIcesiRole(@RequestBody @Valid IcesiRoleDTO roleDTO);
}
