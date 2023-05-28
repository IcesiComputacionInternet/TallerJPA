package co.com.icesi.TallerJpa.controller;

import co.com.icesi.TallerJpa.dto.LoginDTO;
import co.com.icesi.TallerJpa.dto.TokenDTO;
import co.com.icesi.TallerJpa.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @CrossOrigin
    @PostMapping("/token")
    public TokenDTO token(@Valid @RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
        return tokenService.generateToken(authentication);
    }
}
