package com.example.tallerjpa.api;

import com.example.tallerjpa.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserAPI {

    String BASE_USER_URL = "/users";

    @GetMapping("/{userEmail}")
    UserDTO getUser(@PathVariable UserDTO userEmail);
    List<UserDTO> getAllUsers();
    UserDTO addUser(UserDTO userDTO);

}
