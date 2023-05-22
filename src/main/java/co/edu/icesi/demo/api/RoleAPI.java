package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.RoleDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface RoleAPI {

    String BASE_ROLE_URL="/roles";

    @GetMapping("/{roleName}")
    RoleDTO getRole(@PathVariable String roleName);

    @GetMapping
    List<RoleDTO> getAllRoles();

    @PostMapping
    RoleDTO addRole(@Valid @RequestBody RoleDTO roleDTO);

}
