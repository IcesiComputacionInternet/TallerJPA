package com.example.jpa.api;

import com.example.jpa.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(UserAPI.BASE_USER_URL)
public interface UserAPI {

    String BASE_USER_URL = "/users";

    @PostMapping
    UserDTO create(@RequestBody UserDTO userDTO);

    @GetMapping("/{userEmail}")
    UserDTO getUser (@PathVariable("userEmail") String userEmail);

    @GetMapping("/all")
    List<UserDTO> getAllUsers();

}
