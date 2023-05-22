package co.edu.icesi.tallerjpa.api;

import co.edu.icesi.tallerjpa.dto.IcesiRoleResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(IcesiRoleApi.ROLE_BASE_URL)
public interface IcesiRoleApi {

    String ROLE_BASE_URL="/role";

    @GetMapping("/{roleName}")
    IcesiRoleResponseDTO getRole(@PathVariable String roleName);

    List<IcesiRoleResponseDTO> getAllRoles();

    @PostMapping("/createRole")
    IcesiRoleResponseDTO addRole(@Valid @RequestBody IcesiRoleResponseDTO roleCreateDTO);
}
