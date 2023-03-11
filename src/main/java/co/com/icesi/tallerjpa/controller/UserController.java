package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.dto.UserDTO;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add/user")
    public void createUser(@RequestBody UserDTO user){
         userService.save(user);
    }

}
