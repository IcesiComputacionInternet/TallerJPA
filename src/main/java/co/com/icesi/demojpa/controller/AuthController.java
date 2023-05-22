package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.LoginDTO;
import co.com.icesi.demojpa.dto.TokenDTO;
import co.com.icesi.demojpa.security.IcesiSecurityContext;
import co.com.icesi.demojpa.servicio.TokenSercive;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {
    private final TokenSercive tokenSercive;

    private final AuthenticationManager authenticationManager;

    @CrossOrigin
    @PostMapping("/token")
    public TokenDTO token(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
        return tokenSercive.generateToken(authentication);
    }

    @GetMapping("/token/scope")
    public String tokenScope() {
    	return IcesiSecurityContext.getCurrentUserRole();
    }
}
