package com.example.demo.API;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;

public interface IcesiUserAPI {
    
    String BASE_USER_URL = "/icesiUsers";
    
    ResponseIcesiUserDTO add(@Valid @RequestBody String userCreatorRole, IcesiUserCreateDTO requestIcesiUserCreateDTO);

}
