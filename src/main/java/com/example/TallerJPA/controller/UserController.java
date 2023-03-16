package com.example.TallerJPA.controller;

import com.example.TallerJPA.dto.RoleCreateDTO;
import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.model.IcesiRole;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.service.RoleService;
import com.example.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/user")
    public IcesiUser createIcesiUser(@RequestBody UserCreateDTO user){
        return userService.save(user);
    }

    @PostMapping("/role")
    public IcesiRole createIcesiRole(@RequestBody RoleCreateDTO role){
        return roleService.save(role);
    }

}
