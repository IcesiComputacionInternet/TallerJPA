package com.example.jpa.api;

import com.example.jpa.dto.UserRequestDTO;
import com.example.jpa.dto.UserResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserAPI {

    String BASE_USER_URL = "/users";

    @PostMapping
    UserResponseDTO create(@RequestBody UserRequestDTO userDTO);

    @GetMapping("/{userEmail}")
    UserResponseDTO getUser (@PathVariable String userEmail);

    @GetMapping
    List<UserResponseDTO> getAllUsers();
}
