package com.example.TallerJPA.api;

import com.example.TallerJPA.dto.UserDTO;
import com.example.TallerJPA.model.IcesiUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.TallerJPA.api.UserAPI.BASE_USER_URL;

@RequestMapping(BASE_USER_URL)
public interface UserAPI {
    //CAmbiar el void por el DTO
    String BASE_USER_URL = "/users";
    @PostMapping
    UserDTO createIcesiUser(@RequestBody UserDTO user);


}
