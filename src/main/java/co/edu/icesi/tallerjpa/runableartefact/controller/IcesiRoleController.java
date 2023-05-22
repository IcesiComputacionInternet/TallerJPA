package co.edu.icesi.tallerjpa.runableartefact.controller;

import co.edu.icesi.tallerjpa.runableartefact.api.IcesiRoleAPI;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.RoleToAssignDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.DataAlreadyExist;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiRoleController implements IcesiRoleAPI {

    private final IcesiRoleService icesiRoleService;

    private final IcesiRoleMapper icesiRoleMapper;

    @Override
    public String createNewRole(@RequestBody IcesiRoleDTO icesiRole) throws DataAlreadyExist {
        return icesiRoleService.saveNewRole(icesiRole);
    }

    @Override
    public IcesiRoleDTO assignRole(RoleToAssignDTO roleToAssignDTO) {
        return icesiRoleService.assignRoleToIcesiUser(roleToAssignDTO);
    }
}
