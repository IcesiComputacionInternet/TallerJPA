package co.edu.icesi.tallerJPA.controller;

import co.edu.icesi.tallerJPA.dto.IcesiUserDTO;
import co.edu.icesi.tallerJPA.model.IcesiUser;
import co.edu.icesi.tallerJPA.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final IcesiUserService icesiUserService;
    @PostMapping("/add/user")
    public void createUser(@RequestBody IcesiUserDTO user){
         icesiUserService.save(user);
    }
}
