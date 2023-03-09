package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.dto.UserDTO;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/users")
    public void createUser(@RequestBody UserDTO user){
        userService.save(user);

    }

}
