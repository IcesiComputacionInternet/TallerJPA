package com.edu.icesi.TallerJPA.controller;

import com.edu.icesi.TallerJPA.api.UserAPI;
import com.edu.icesi.TallerJPA.dto.IcesiUserDTO;
import com.edu.icesi.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    public IcesiUserDTO getUser(String email) {
        return userService.findByEmail(email);
    }

    public List<IcesiUserDTO> getAllUsers(){
        return null;
    }

    @Override
    public IcesiUserDTO createUser(IcesiUserDTO userCreateDTO) {
        return userService.save(userCreateDTO);
    }

    @Override
    public IcesiUserDTO getUserByPhoneNumber(String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber);
    }

}
