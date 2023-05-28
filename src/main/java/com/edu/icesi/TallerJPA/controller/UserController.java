package com.edu.icesi.TallerJPA.controller;

import com.edu.icesi.TallerJPA.api.UserAPI;
import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edu.icesi.TallerJPA.api.UserAPI.BASE_USER_URL;

@RestController
@RequestMapping(BASE_USER_URL)
@AllArgsConstructor
public class UserController implements UserAPI {

    private final UserService userService;

    @Override
    public UserCreateDTO getUser(String email) {
        return userService.findByEmail(email);
    }

    public List<UserCreateDTO> getAllUsers(){
        return null;
    }

    @Override
    public UserCreateDTO createUser(UserCreateDTO userCreateDTO) {
        return userService.save(userCreateDTO);
    }

    @Override
    public UserCreateDTO getUserByPhoneNumber(String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<AccountCreateDTO> getAccounts() {
        return userService.getAccounts();
    }

}
