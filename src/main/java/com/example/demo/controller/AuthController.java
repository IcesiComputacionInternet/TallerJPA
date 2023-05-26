package com.example.demo.controller;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.TokenDTO;
import com.example.demo.service.TokenService;
import org.springframework.security.core.Authentication ;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {
    
    private final  TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @CrossOrigin
    @PostMapping("/token")
    public TokenDTO token(@RequestBody LoginDTO LoginDTO) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(LoginDTO.username(), LoginDTO.password()));
        return TokenDTO.builder().token(tokenService.generateToken(authentication)).build();
    }
}
