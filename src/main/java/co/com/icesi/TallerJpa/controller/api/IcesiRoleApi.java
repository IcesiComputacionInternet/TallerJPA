package co.com.icesi.TallerJpa.controller.api;

import co.com.icesi.TallerJpa.dto.IcesiRoleDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = IcesiRoleApi.ROLE_BASE_URL)
public interface IcesiRoleApi {

    String ROLE_BASE_URL = "/api/role";

    @PostMapping
    IcesiRoleDTO addIcesiRole(@Valid @RequestBody IcesiRoleDTO icesiRoleDTO);

    @PostMapping("/roles")
    List<IcesiRoleDTO> addListIcesiRole(@Valid @RequestBody List<IcesiRoleDTO> icesiRoleDTOS);

    @GetMapping("/roles")
    List<IcesiRoleDTO> getAllIcesiRoles();

    @GetMapping("/{name}")
    IcesiRoleDTO getIcesiRole(@PathVariable String name);
}
