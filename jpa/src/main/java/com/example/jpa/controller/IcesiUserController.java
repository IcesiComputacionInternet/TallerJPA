package com.example.jpa.controller;

import com.example.jpa.api.UserAPI;
import com.example.jpa.dto.UserDTO;
import com.example.jpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.jpa.api.UserAPI.BASE_USER_URL;

@RestController
@AllArgsConstructor
@RequestMapping(BASE_USER_URL)
public class IcesiUserController implements UserAPI {

    UserService userService;

    @Override
    public UserDTO create(@RequestBody UserDTO userDTO){
        return userService.save(userDTO);
    }

    @Override
    public List<UserDTO> getAllUsers(){
        return userService.getUsers();
    }

    @Override
    public UserDTO getUser(String userEmail){
        return userService.getUser(userEmail);
    }
}
