package com.icesi.TallerJPA.controller;

import com.icesi.TallerJPA.dto.request.IcesiLoginDTO;
import com.icesi.TallerJPA.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String token(@RequestBody IcesiLoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
        return tokenService.generateToken(authentication);
    }

}
