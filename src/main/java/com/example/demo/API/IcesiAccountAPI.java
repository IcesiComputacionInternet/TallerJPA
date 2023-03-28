package com.example.demo.API;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;

public interface IcesiAccountAPI {
    
    String BASE_URL = "/accounts";

    @PostMapping("/add")
    ResponseIcesiAccountDTO save(@RequestBody IcesiAccountCreateDTO account);
}
