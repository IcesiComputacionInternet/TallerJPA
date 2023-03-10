package co.edu.icesi.tallerjpa.runableartefact.controller;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiUserController {

    private final IcesiUserService icesiUserService;

    @PostMapping("/icesiuser/create")
    public String createNewUser(@RequestBody IcesiUserDTO icesiUser){
        return icesiUserService.saveNewUser(icesiUser);
    }
}
