package com.edu.icesi.TallerJPA.controller;

import com.edu.icesi.TallerJPA.api.UserAPI;
import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edu.icesi.TallerJPA.api.UserAPI.BASE_USER_URL;

@RestController(BASE_USER_URL)
@AllArgsConstructor
public class UserController implements UserAPI {

    private final UserService userService;

    @Override
    public UserCreateDTO getUser(String email) {
        return userService.getByEmail(email);
    }

    public List<UserCreateDTO> getAllUsers(){
        return null;
    }

    @Override
    public UserCreateDTO addUser(UserCreateDTO userCreateDTO) {
        return userService.save(userCreateDTO);
    }

    @Override
    public UserCreateDTO getUserByPhoneNumber(String phoneNumber) {
        return userService.getByPhoneNumber(phoneNumber);
    }

}
