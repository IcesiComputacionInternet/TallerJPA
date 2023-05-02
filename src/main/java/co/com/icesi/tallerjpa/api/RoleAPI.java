package co.com.icesi.tallerjpa.api;
import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RoleAPI {
    String ROLE_URL = "/roles";

    @GetMapping("/{roleName}")
    RoleCreateDTO getRole(@PathVariable String roleName);

    List<RoleCreateDTO> getAllRoles();

    @PostMapping("/createRole")
    RoleCreateDTO addRole(@RequestBody RoleCreateDTO roleCreateDTO);
}
