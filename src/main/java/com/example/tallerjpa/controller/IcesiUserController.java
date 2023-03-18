package com.example.tallerjpa.controller;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.model.IcesiUser;
import com.example.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiUserController {

    private final UserService userService;

    @PostMapping("/user")
    public IcesiUser createIcesiUser(@RequestBody UserDTO userDTO){
        return userService.saveIcesiUser(userDTO);
    }


}
