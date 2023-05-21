package com.edu.icesi.TallerJPA.controller;

import com.edu.icesi.TallerJPA.dto.LoginDTO;
import com.edu.icesi.TallerJPA.dto.TokenDTO;
import com.edu.icesi.TallerJPA.security.CustomAuthentication;
import com.edu.icesi.TallerJPA.security.IcesiSecurityContext;
import com.edu.icesi.TallerJPA.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public TokenDTO token(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.userName(), loginDTO.password()));
        return tokenService.generateToken(authentication);
    }
}
