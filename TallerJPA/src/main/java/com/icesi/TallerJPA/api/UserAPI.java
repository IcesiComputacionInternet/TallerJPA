package com.icesi.TallerJPA.api;

import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import com.icesi.TallerJPA.dto.response.IcesiUserResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(UserAPI.USER_BASE_URL)
public interface UserAPI {
    String USER_BASE_URL = "/user";
    @PostMapping
    IcesiUserResponseDTO createIcesiUser(@RequestBody IcesiUserDTO user);
}
