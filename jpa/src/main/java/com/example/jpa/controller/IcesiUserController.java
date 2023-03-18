package com.example.jpa.controller;

import com.example.jpa.dto.UserRequestDTO;
import com.example.jpa.dto.UserResponseDTO;
import com.example.jpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class IcesiUserController {

    UserService userService;

    @PostMapping
    public UserResponseDTO create(@RequestBody UserRequestDTO userDTO){
        return userService.save(userDTO);
    }

    @GetMapping
    public List<UserResponseDTO> getIcesiUser(){
        return userService.getUsers();
    }
}
