package co.com.icesi.jpataller.controller;

import co.com.icesi.jpataller.dto.IcesiUserDTO;
import co.com.icesi.jpataller.model.IcesiUser;
import co.com.icesi.jpataller.service.IcesiUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IcesiUserController {

    private final IcesiUserService icesiUserService;

    public IcesiUserController(IcesiUserService icesiUserService) {
        this.icesiUserService = icesiUserService;
    }
    @PostMapping("/users")
    public IcesiUser createUser(@RequestBody IcesiUserDTO userDTO) {
        return icesiUserService.createUser(userDTO);
    }

}
