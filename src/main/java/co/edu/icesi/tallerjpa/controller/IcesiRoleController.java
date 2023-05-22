package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.exception.DataAlreadyExistException;
import co.edu.icesi.tallerjpa.exception.InvalidDataException;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class IcesiRoleController {

    private final IcesiRoleService icesiRoleService;


    @PostMapping("/icesirole/create")
    public IcesiRole createNewRole(@RequestBody IcesiRoleDTO icesiRole) throws RuntimeException, InvalidDataException, DataAlreadyExistException {
        return icesiRoleService.createRole(icesiRole);
    }
}
