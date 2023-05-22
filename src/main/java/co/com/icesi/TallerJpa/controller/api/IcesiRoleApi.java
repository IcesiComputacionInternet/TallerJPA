package co.com.icesi.TallerJpa.controller.api;

import co.com.icesi.TallerJpa.dto.IcesiRoleDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RequestMapping(value = IcesiRoleApi.ROLE_BASE_URL)
@Validated
public interface IcesiRoleApi {

    String ROLE_BASE_URL = "/api/roles";

    @PostMapping
    IcesiRoleDTO addIcesiRole(@Valid @RequestBody IcesiRoleDTO icesiRoleDTO);

    @PostMapping(params = "list=true")
    List<IcesiRoleDTO> addListIcesiRole(@Valid @RequestBody List<IcesiRoleDTO> icesiRoleDTOS);

    @GetMapping(params = "all=true")
    List<IcesiRoleDTO> getAllIcesiRoles();

    @GetMapping("/{name}")
    IcesiRoleDTO getIcesiRole(@NotBlank @Valid @PathVariable String name);
}
