package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(RoleAPI.ROLE_BASE_URL)
public interface RoleAPI {
    String ROLE_BASE_URL = "/roles";

    @PostMapping
    RoleCreateDTO addRole(@Valid @RequestBody RoleCreateDTO role);

    @GetMapping("/{roleId}")
    RoleCreateDTO getRoleById(@Valid @PathVariable String roleId);
}
