package com.example.TallerJPA.controller;

import com.example.TallerJPA.api.UserAPI;
import com.example.TallerJPA.dto.UserDTO;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.service.RoleService;
import com.example.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.example.TallerJPA.api.UserAPI.BASE_USER_URL;

@RestController
@AllArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;
    private final RoleService roleService;


    @Override
    public UserDTO createIcesiUser(@Valid UserDTO user) {
        return userService.save(user);
    }

}
