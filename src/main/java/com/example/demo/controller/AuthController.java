package com.example.demo.controller;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.service.TokenService;
import org.springframework.security.core.Authentication ;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {
    
    private final  TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/token")
    public String token(@RequestBody LoginDTO LoginDTO) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(LoginDTO.username(), LoginDTO.password()));
        return tokenService.generateToken(authentication);
    }
}
