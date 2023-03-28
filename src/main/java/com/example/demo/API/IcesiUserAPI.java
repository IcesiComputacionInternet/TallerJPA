package com.example.demo.API;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;

public interface IcesiUserAPI {
    
    String BASE_USER_URL = "/icesiUsers";

    //@GetMapping("/{icesiUserEmail}")
    //IcesiUserCreateDTO getUser(@PathVariable String icesiUserEmail);

    @PostMapping("/add")
    ResponseIcesiUserDTO add(IcesiUserCreateDTO requestIcesiUserCreateDTO);
}
