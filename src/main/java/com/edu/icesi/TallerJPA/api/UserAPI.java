package com.edu.icesi.TallerJPA.api;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UserAPI {

    String BASE_USER_URL = "/users";

    @GetMapping("getEmail/{email}/")
    UserCreateDTO getUser(@PathVariable("email") String email);

    List<UserCreateDTO> getAllUsers();

    @PostMapping("/create/")
    UserCreateDTO createUser(@Valid @RequestBody UserCreateDTO userCreateDTO);

    @GetMapping("getPhone/{phoneNumber}/")
    UserCreateDTO getUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber);

    @GetMapping("/getAccounts/")
    List<AccountCreateDTO> getAccounts();

}
