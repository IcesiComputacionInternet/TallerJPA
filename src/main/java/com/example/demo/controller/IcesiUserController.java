package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.API.IcesiUserAPI;
import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;
import com.example.demo.service.IcesiUserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class IcesiUserController implements IcesiUserAPI {
    
    private final IcesiUserService icesiUserService;

    /*@Override
    public IcesiUserCreateDTO getUser(String userEmail) {
        return null;
    }

    @Override
    public List<IcesiUserCreateDTO> getAllUsers() {
        return null;
    }

    @Override
    public IcesiUserCreateDTO addUser(IcesiUserCreateDTO requestIcesiUserCreateDTO) {
        return null;
    }*/
    
    public ResponseIcesiUserDTO add(@RequestBody IcesiUserCreateDTO user) {
        return icesiUserService.create(user);
    }
}
