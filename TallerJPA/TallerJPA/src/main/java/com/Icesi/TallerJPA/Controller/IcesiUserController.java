package com.Icesi.TallerJPA.Controller;

import com.Icesi.TallerJPA.api.UserApi;
import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("BASE_USER_URL")
@AllArgsConstructor
public class IcesiUserController implements UserApi {
    private final IcesiUserService userService;
    @PostMapping
    public IcesiUserDTO createUser (@RequestBody IcesiUserDTO user){
        return  userService.save(user);
    }

    @Override
    public IcesiUserDTO getUser(String userEmail) {
        return null;
    }

    @GetMapping
    public List<IcesiUserDTO> getAllUsers(){
        return null;
    }

    @Override
    public IcesiUserDTO addUser(IcesiUserDTO icesiUserDTO) {
        return null;
    }

    @GetMapping("/{userId}")
    public IcesiUserDTO getUser(){
        return  null;
    }

}
