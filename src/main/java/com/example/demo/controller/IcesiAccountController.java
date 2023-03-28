package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.API.IcesiAccountAPI;
import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.service.IcesiAccountService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class IcesiAccountController implements IcesiAccountAPI {
    
    private final IcesiAccountService icesiAccountService;

    public ResponseIcesiAccountDTO save(@RequestBody IcesiAccountCreateDTO account) {
        return icesiAccountService.create(account);
    }

}
