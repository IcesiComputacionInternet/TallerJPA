package com.example.tallerjpa.api;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.model.IcesiUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(UserAPI.BASE_USER_URL)
public interface UserAPI {

    String BASE_USER_URL = "/users";

    @PostMapping
    IcesiUser createUser(@RequestBody @Valid UserDTO userDTO);




}
