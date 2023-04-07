package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.api.UserAPI;
import co.com.icesi.demojpa.dto.RequestUserDTO;
import co.com.icesi.demojpa.dto.ResponseUserDTO;
import co.com.icesi.demojpa.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/users")
public class UserController implements UserAPI {

    private UserService userService ;

    @Override
    public ResponseUserDTO getUser(String userEmail) {
        return null;
    }

    @GetMapping
    public List<ResponseUserDTO> getAllUsers() {
        return null;
    }

    @Override
    public ResponseUserDTO addUser(RequestUserDTO requestUserDTO) {
        return null;
    }
}
