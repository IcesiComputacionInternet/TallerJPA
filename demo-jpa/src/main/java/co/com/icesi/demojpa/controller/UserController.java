package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.api.UserAPI;
import co.com.icesi.demojpa.dto.ResponseUserDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController implements UserAPI {

    private UserService userService ;

    @Override
    public UserCreateDTO addUser(@Valid @RequestBody UserCreateDTO requestUserDTO) {
        return userService.save(requestUserDTO);
    }

    @Override
    public UserCreateDTO getUser(String userEmail) {
        return userService.getByEmail(userEmail);
    }

}
