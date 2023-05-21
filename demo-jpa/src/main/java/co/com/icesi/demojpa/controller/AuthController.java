package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.LoginDTO;
import co.com.icesi.demojpa.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@RestController
public class AuthController {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @CrossOrigin
    @GetMapping("/token")
    public String token(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
    return tokenService.generateToken(authentication);

    }

}
