package co.com.icesi.jpataller.controller;

import co.com.icesi.jpataller.dto.IcesiRoleDTO;
import co.com.icesi.jpataller.model.IcesiRole;
import co.com.icesi.jpataller.service.IcesiRoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IcesiRoleController {

    private final IcesiRoleService icesiRoleService;

    public IcesiRoleController(IcesiRoleService icesiRoleService) {
        this.icesiRoleService = icesiRoleService;
    }
    @PostMapping("/roles")
    public IcesiRole createRole(@RequestBody IcesiRoleDTO roleDTO) {
        return icesiRoleService.createRole(roleDTO);
    }

}
