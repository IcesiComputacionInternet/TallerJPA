package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.exception.DuplicateDataException;
import co.edu.icesi.tallerjpa.exception.MissingParameterException;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@RestController
@AllArgsConstructor
public class IcesiUserController {

    private final IcesiUserService icesiUserService;


    @PostMapping("/icesiuser/create")
    public IcesiUser createNewUser(@RequestBody IcesiUserDTO icesiUser) throws RoleNotFoundException, DuplicateDataException, MissingParameterException {
        return icesiUserService.saveNewUser(icesiUser);
    }

}
