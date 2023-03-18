package com.Icesi.TallerJPA.api;

import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/account")
@CrossOrigin(origins = "*")
public interface AccountAPI {

    @PostMapping("/users")
    IcesiUserDTO createUser(@RequestBody IcesiUserDTO icesiUserDTO);

    @GetMapping("/users")
    List<IcesiUserDTO> getUsers();

    @PostMapping("/roles")
    IcesiRole createRole(@RequestBody IcesiRoleDTO icesiRoleDTO);

    @GetMapping("/roles")
    List<IcesiRole> getRoles();
}