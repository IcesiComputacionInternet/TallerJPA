package com.edu.icesi.TallerJPA.api;

import com.edu.icesi.TallerJPA.dto.IcesiUserDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface UserAPI {

    String BASE_USER_URL = "/users";

    @GetMapping("/{email}")
    IcesiUserDTO getUser(@PathVariable String email);
    @GetMapping

    List<IcesiUserDTO> getAllUsers();

    @PostMapping("/create/")
    IcesiUserDTO createUser(@Valid @RequestBody IcesiUserDTO userCreateDTO);

    @GetMapping("/{phoneNumber}/")
    IcesiUserDTO getUserByPhoneNumber(@PathVariable String phoneNumber);

}
