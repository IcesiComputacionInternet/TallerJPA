package co.com.icesi.demojpa.controller;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.servicio.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users")
    public IcesiUser createIcesiUser(@RequestBody UserCreateDTO user){
        return userService.save(user);
    }

    @GetMapping("/users/{id}")
    public IcesiUser returnUser(@PathVariable String id){
        return userService.findById(UUID.fromString(id)).orElseThrow();
    }


}
