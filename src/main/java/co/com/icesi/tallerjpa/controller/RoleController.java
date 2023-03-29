package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.RoleApi;
import co.com.icesi.tallerjpa.dto.RoleDTO;
import co.com.icesi.tallerjpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class RoleController implements RoleApi {

    private RoleService roleService;

    @Override
    public RoleDTO add(@Valid @RequestBody RoleDTO role) {
        return roleService.save(role);
    }
}
