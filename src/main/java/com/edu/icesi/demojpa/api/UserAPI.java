package com.edu.icesi.demojpa.api;

import com.edu.icesi.demojpa.dto.request.RequestUserDTO;
import com.edu.icesi.demojpa.dto.response.ResponseUserDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface UserAPI {
    String BASE_USER_URL = "/users";
    @GetMapping("/{userEmail}")
    ResponseUserDTO getUser(@PathVariable String userEmail);
    @GetMapping
    List<ResponseUserDTO> getAllUsers();
    @PostMapping("/create")
    ResponseUserDTO createUser(@Valid @RequestBody RequestUserDTO requestUserDTO);
}
