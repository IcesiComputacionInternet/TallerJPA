package com.example.demo.api;

import com.example.demo.DTO.IcesiUserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
public interface IcesiUserApi {
    @PostMapping
    IcesiUserDTO createIcesiUser(@RequestBody IcesiUserDTO user);
}
