package com.Icesi.TallerJPA.api;

import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RequestMapping(UserAPI.BASE_USER_URL)
public interface UserAPI {
    String BASE_USER_URL  = "/users";

    @PostMapping("/create/")
    IcesiUserDTO createIcesiUser(@Valid @RequestBody IcesiUserDTO icesiUserDTO);

    @GetMapping
    List<IcesiUserDTO> getAllUsers();

    @GetMapping("/{userEmail}")
    IcesiUserDTO getUserByEmail (@PathVariable String userEmail);
}
