package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/addRole")
    public IcesiRole createIcesiRole(@RequestBody RoleCreateDTO role) {
        return roleService.save(role);
    }
}
