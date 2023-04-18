package com.example.TallerJPA.api;

import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.model.IcesiUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserAPI {
    //CAmbiar el void por el DTO
    String BASE_USER_URL = "/users";
    @PostMapping("/user")
    IcesiUser createIcesiUser(@RequestBody UserCreateDTO user);
    @GetMapping("/{userEmail}")
    void getUser(@PathVariable String userEmail);
    List<IcesiUser> getAllUsers();

}
