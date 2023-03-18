package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/add")
    public IcesiUser createIcesiUser(@RequestBody UserCreateDTO user){

        return userService.save(user);
    }

    @GetMapping("/users/{id}")
    public IcesiUser getIcesiUserById(@PathVariable String id){

        return userService.findById(UUID.fromString(id)).orElseThrow();

    }
}
