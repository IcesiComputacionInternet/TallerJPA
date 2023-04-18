package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiRole;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiRoleAPI.BASE_ROLE_URL;

@RequestMapping(value = BASE_ROLE_URL)
public interface IcesiRoleAPI {

    String BASE_ROLE_URL = "/roles";

    @GetMapping("/{roleName}")
    IcesiRole getRoleByName(@PathVariable String roleName);

    @GetMapping("/all")
    List<IcesiRole> getAllRoles();

    @PostMapping
    IcesiRole createIcesiRole(@RequestBody RoleCreateDTO role);
}
