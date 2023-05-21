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

    @PostMapping("add/user")
    public UserCreateDTO createUser(@Valid @RequestBody UserCreateDTO requestUserDTO) {
        return userService.save(requestUserDTO);
    }

    @Override
    public ResponseUserDTO getUser(String userEmail) {
        return null;
    }

    @GetMapping
    public List<ResponseUserDTO> getAllUsers() {
        return null;
    }

    @Override
    public ResponseUserDTO addUser(UserCreateDTO requestUserDTO) {
        return null;
    }
}
