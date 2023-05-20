package co.com.icesi.icesiAccountSystem.api;

import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RequestMapping(RoleAPI.BASE_ROLE_URL)
public interface RoleAPI {

    String BASE_ROLE_URL="/roles";
    @GetMapping("/{name}")
    RoleDTO getRole(@PathVariable String roleName);
    List<RoleDTO> getAllRoles();
    @PostMapping("/create")
    RoleDTO createRole(@Valid @RequestBody RoleDTO roleDTO);
}
