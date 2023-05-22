package co.edu.icesi.tallerjpa.runableartefact.api;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.RoleToAssignDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(IcesiRoleAPI.BASE_URL)
public interface IcesiRoleAPI {

    String BASE_URL = "/api/icesi-roles";

    @PostMapping("/save")
    String createNewRole(@RequestBody IcesiRoleDTO roleDTO);

    @PostMapping("/assign-role")
    IcesiRoleDTO assignRole(@RequestBody RoleToAssignDTO roleToAssignDTO);
}
