package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.dto.UserDTO;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add/user")
    public IcesiUser createUser(@RequestBody UserDTO user){
        return userService.save(user);
    }

}
