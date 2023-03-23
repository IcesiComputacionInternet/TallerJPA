package com.example.TallerJPA.controller;

import com.example.TallerJPA.api.UserAPI;
import com.example.TallerJPA.dto.RoleCreateDTO;
import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.model.IcesiRole;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.service.RoleService;
import com.example.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.TallerJPA.api.UserAPI.BASE_USER_URL;

@RestController(BASE_USER_URL)
@AllArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/user")
    public IcesiUser createIcesiUser(@RequestBody UserCreateDTO user){
        return userService.save(user);
    }


    @Override
    public void getUser(String userEmail) {

    }

    @GetMapping
    public List<IcesiUser> getAllUsers(){

        return null;
    }

    @Override
    public void addUser(UserCreateDTO user) {

    }

    //En vez de IcesiUser poner el DTO
    @GetMapping("/{userId}")
    public IcesiUser getUser(){
        return null;
    }


}
