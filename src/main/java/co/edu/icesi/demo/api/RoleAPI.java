package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.RoleCreateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface RoleAPI {

    String BASE_ROLE_URL="/roles";

    @GetMapping("/{roleName}")
    RoleCreateDTO getRole(@PathVariable String roleName);

    @GetMapping
    List<RoleCreateDTO> getAllRoles();

    @PostMapping
    RoleCreateDTO addRole(@Valid @RequestBody RoleCreateDTO roleCreateDTO);

}
