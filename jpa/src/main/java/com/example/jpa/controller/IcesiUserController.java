package com.example.jpa.controller;

import com.example.jpa.dto.UserRequestDTO;
import com.example.jpa.dto.UserResponseDTO;
import com.example.jpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.jpa.api.UserAPI.BASE_USER_URL;

@RestController
@AllArgsConstructor
@RequestMapping(BASE_USER_URL)
public class IcesiUserController {

    UserService userService;

    @PostMapping
    public UserResponseDTO create(@RequestBody UserRequestDTO userDTO){
        return userService.save(userDTO);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userEmail}")
    public UserResponseDTO getUser(@PathVariable String userEmail){
        return userService.getUser(userEmail);
    }
}
