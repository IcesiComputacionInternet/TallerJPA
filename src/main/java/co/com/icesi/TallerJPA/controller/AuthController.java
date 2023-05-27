package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.requestDTO.LoginDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.TokenDTO;
import co.com.icesi.TallerJPA.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public TokenDTO token(@RequestBody LoginDTO logindDTO){
        Authentication authentication=authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(logindDTO.username(), logindDTO.password()));
        return tokenService.generateToken(authentication);
    }
}
