package com.example.TallerJPA.controller;

import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    public IcesiUser createIcesiUser(@RequestBody UserCreateDTO user){
        return userService.save(user);
    }



}
