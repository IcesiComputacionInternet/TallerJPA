package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.model.IcesiRole;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/Roles")
public interface RoleApi {
    @PostMapping("/add/role")
    public IcesiRole createRole(@Valid @RequestBody IcesiRoleDto role);
}
