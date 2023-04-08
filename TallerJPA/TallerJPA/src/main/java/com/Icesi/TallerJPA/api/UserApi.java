package com.Icesi.TallerJPA.api;

import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@RequestMapping(UserApi.BASE_USER_URL)
public interface UserApi {
    String BASE_USER_URL  = "/users";

    @GetMapping("/{userEmail}")
    IcesiUserDTO getUser (@PathVariable String userEmail);
    List<IcesiUserDTO> getAllUsers();
    IcesiUserDTO addUser(IcesiUserDTO icesiUserDTO);

}
