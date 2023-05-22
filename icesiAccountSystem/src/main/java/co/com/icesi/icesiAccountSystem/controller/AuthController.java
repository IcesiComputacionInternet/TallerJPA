package co.com.icesi.icesiAccountSystem.controller;

import co.com.icesi.icesiAccountSystem.dto.LoginDTO;
import co.com.icesi.icesiAccountSystem.dto.TokenDTO;
import co.com.icesi.icesiAccountSystem.service.TokenService;
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

    @PostMapping("/login")
    public TokenDTO token(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
        return tokenService.generateToken(authentication);
    }
}
