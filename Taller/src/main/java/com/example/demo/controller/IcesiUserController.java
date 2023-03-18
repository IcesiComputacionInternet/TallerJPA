package com.example.demo.controller;

import com.example.demo.DTO.IcesiUserDTO;
import com.example.demo.model.IcesiUser;
import com.example.demo.service.IcesiUserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class IcesiUserController {
    private final IcesiUserService userService;

    public IcesiUserController(IcesiUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public IcesiUser createIcesiUser(@RequestBody IcesiUserDTO user) {
        return userService.createUser(user);
    }

    @GetMapping("/users/{id}")
    public IcesiUser getUserById(@PathVariable String id) {
        return userService.findById(UUID.fromString(id)).orElseThrow();
    }
}
