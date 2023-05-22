package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.RoleApi;
import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class RoleController implements RoleApi {

    private IcesiRoleService icesiRoleService;

    @Override
    public IcesiRole createRole(IcesiRoleDto role) {
        return icesiRoleService.saveRole(role);
    }
}
