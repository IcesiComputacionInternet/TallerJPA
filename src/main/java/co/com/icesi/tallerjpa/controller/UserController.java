package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add/user")
    public ResponseUserDTO createUser(@RequestBody RequestUserDTO user){
        return userService.save(user);
    }

}
