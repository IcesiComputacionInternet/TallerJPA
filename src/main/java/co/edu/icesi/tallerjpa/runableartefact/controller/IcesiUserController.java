package co.edu.icesi.tallerjpa.runableartefact.controller;

import co.edu.icesi.tallerjpa.runableartefact.api.IcesiUserAPI;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class IcesiUserController implements IcesiUserAPI {

    private final IcesiUserService icesiUserService;
    @Override
    public String createNewUser(@RequestBody IcesiUserDTO icesiUser){
        return icesiUserService.saveNewUser(icesiUser);
    }

    @Override
    public IcesiUserDTO updateUser(@RequestBody IcesiUserDTO userDTO) {
        return icesiUserService.updateIcesiUser(userDTO);
    }

    @Override
    public List<IcesiUser> getAllUsers() {
        return icesiUserService.getAllUsers();
    }
}
