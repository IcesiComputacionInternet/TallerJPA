package co.com.icesi.demojpa.controller;


import co.com.icesi.demojpa.api.RoleAPI;
import co.com.icesi.demojpa.dto.request.RoleCreateDTO;
import co.com.icesi.demojpa.dto.response.RoleResponseDTO;
import co.com.icesi.demojpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class RoleController implements RoleAPI {

    private final RoleService roleService;

    @Override
    public RoleResponseDTO createIcesiRole(RoleCreateDTO role) {
        return roleService.save(role);
    }
}
