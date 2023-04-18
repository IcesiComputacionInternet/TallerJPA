package com.icesi.TallerJPA.controller;

import com.icesi.TallerJPA.api.UserAPI;
import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import com.icesi.TallerJPA.dto.response.IcesiUserResponseDTO;
import com.icesi.TallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.icesi.TallerJPA.service.UserService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class IcesiUserController implements UserAPI {

    private final UserService userService;

    @Override
    public IcesiUserResponseDTO createIcesiUser(@Valid IcesiUserDTO user) {
        return userService.save(user);
    }
}
