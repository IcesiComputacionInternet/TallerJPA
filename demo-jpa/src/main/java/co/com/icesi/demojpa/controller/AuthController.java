package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.LoginDTO;
import co.com.icesi.demojpa.dto.TokenDTO;
import co.com.icesi.demojpa.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin
public class AuthController {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public TokenDTO token(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
    return TokenDTO.builder().token(tokenService.generateToken(authentication)).build();

    }

}
