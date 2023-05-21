package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final IcesiUserService icesiUserService;

    @PostMapping("/add/user")
    public IcesiUserDto createUser(@RequestBody IcesiUserDto user){
        return icesiUserService.saveUser(user);
    }
}
