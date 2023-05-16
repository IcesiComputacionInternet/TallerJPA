package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.dto.LoginDTO;
import co.edu.icesi.tallerjpa.dto.TokenDTO;
import co.edu.icesi.tallerjpa.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/token")
    public TokenDTO token(@Valid @RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
        return tokenService.generateToken(authentication);
    }
}
