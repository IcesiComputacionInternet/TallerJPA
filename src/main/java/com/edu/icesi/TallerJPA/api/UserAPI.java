package com.edu.icesi.TallerJPA.api;

import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UserAPI {

    String BASE_USER_URL = "/users";

    @GetMapping("/{email}/")
    UserCreateDTO getUser(@PathVariable String email);

    List<UserCreateDTO> getAllUsers();

    @PostMapping("/create/")
    UserCreateDTO addUser(@Valid @RequestBody UserCreateDTO userCreateDTO);

    @GetMapping("/{phoneNumber}/")
    UserCreateDTO getUserByPhoneNumber(@PathVariable String phoneNumber);

}
