package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.dto.LoginDto;
import co.edu.icesi.demo.dto.TokenDto;
import co.edu.icesi.demo.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @CrossOrigin(origins="http://localhost:5173")
    @PostMapping("/token")
    public TokenDto token(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(),loginDto.password()));
        //return tokenService.generateToken(authentication);
       return TokenDto.builder().token(tokenService.generateToken(authentication)).build();
    }
}
