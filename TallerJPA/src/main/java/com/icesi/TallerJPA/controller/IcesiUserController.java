package com.icesi.TallerJPA.controller;

import com.icesi.TallerJPA.dto.IcesiUserDTO;
import com.icesi.TallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.icesi.TallerJPA.service.UserService;

@RestController
@AllArgsConstructor
public class IcesiUserController {

    private final UserService userService;

    @PostMapping("/user")
    public IcesiUser createIcesiUser(@RequestBody IcesiUserDTO user){
        return userService.save(user);
    }

}
