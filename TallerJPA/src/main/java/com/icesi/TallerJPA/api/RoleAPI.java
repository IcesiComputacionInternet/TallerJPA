package com.icesi.TallerJPA.api;

import com.icesi.TallerJPA.dto.request.IcesiRoleDTO;
import com.icesi.TallerJPA.dto.response.IcesiRoleResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(RoleAPI.ROLE_BASE_URL)
public interface RoleAPI {
    String ROLE_BASE_URL = "/role";
    @PostMapping
    IcesiRoleResponseDTO createIcesiRole(@RequestBody IcesiRoleDTO role);
}
