package co.edu.icesi.tallerjpa.runableartefact.controller;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.DataAlreadyExist;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiRoleController {

    private final IcesiRoleService icesiRoleService;

    private final IcesiRoleMapper icesiRoleMapper;

    @PostMapping("/icesirole/create")
    public String createNewRole(@RequestBody IcesiRoleDTO icesiRole) throws DataAlreadyExist {
        return icesiRoleService.saveNewRole(icesiRoleMapper.toIcesiRole(icesiRole));
    }
}
