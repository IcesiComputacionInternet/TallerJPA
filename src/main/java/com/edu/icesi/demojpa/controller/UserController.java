package com.edu.icesi.demojpa.controller;

import com.edu.icesi.demojpa.api.UserAPI;
import com.edu.icesi.demojpa.dto.RequestUserDTO;
import com.edu.icesi.demojpa.dto.ResponseUserDTO;
import com.edu.icesi.demojpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edu.icesi.demojpa.api.UserAPI.BASE_USER_URL;

@RestController(BASE_USER_URL)
@AllArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;
    @Override
    public ResponseUserDTO getUser(String userEmail) {
        return userService.getUser(userEmail);
    }

    @Override
    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public ResponseUserDTO createUser(RequestUserDTO requestUserDTO) {
        return userService.save(requestUserDTO);
    }
}
