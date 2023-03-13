package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.dto.CreatedUserDTO;
import co.com.icesi.tallerjpa.dto.SendUserDTO;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add/user")
    public SendUserDTO createUser(@RequestBody CreatedUserDTO user){
        return userService.save(user);
    }

}
