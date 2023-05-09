package co.com.icesi.demojpa.controller;
import co.com.icesi.demojpa.api.UserAPI;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.servicio.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static co.com.icesi.demojpa.api.UserAPI.BASE_USER_URL;

@RestController(BASE_USER_URL)
public class UserController implements UserAPI {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/")
    public ResponseUserDTO createIcesiUser(@RequestBody UserCreateDTO user){
        return userService.save(user);
    }




}
