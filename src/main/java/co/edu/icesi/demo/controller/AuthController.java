package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.dto.LoginDTO;
import co.edu.icesi.demo.dto.TokenDTO;
import co.edu.icesi.demo.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static co.edu.icesi.demo.security.IcesiSecurityContext.getCurrentUserId;

@RestController
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private final TokenService tokenService;

    private final AuthenticationManager authenticatorManager;

    @PostMapping("/token")
    public TokenDTO token(@RequestBody LoginDTO loginDTO){
        Authentication authentication =authenticatorManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(),loginDTO.password()));
        return tokenService.generateToken(authentication);
    }

}
