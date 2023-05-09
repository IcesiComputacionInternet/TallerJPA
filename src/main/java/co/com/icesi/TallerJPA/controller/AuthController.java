package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.LoginDTO;
import co.com.icesi.TallerJPA.service.security.TokenService;
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

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public String token(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(),loginDTO.password()));
        return tokenService.generateToken(authentication);
    }
}
