package com.edu.icesi.demojpa.api;

import com.edu.icesi.demojpa.dto.RequestUserDTO;
import com.edu.icesi.demojpa.dto.ResponseUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserAPI {
    String BASE_USER_URL = "/users";
    @GetMapping("/{userEmail}")
    ResponseUserDTO getUser(@PathVariable String userEmail);
    @GetMapping
    List<ResponseUserDTO> getAllUsers();
    @PostMapping("/createUser")
    ResponseUserDTO createUser(@RequestBody RequestUserDTO requestUserDTO);
}
