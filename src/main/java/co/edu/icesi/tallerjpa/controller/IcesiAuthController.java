package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.config.IcesiAuthenticationManager;
import co.edu.icesi.tallerjpa.dto.IcesiLoginDTO;
import co.edu.icesi.tallerjpa.dto.IcesiTokenDTO;
import co.edu.icesi.tallerjpa.service.IcesiTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiAuthController {
    private final IcesiTokenService tokenService;

    private final IcesiAuthenticationManager authenticationManager;

    @CrossOrigin(origins = "http://localocalhost:5173")
    @PostMapping("/token")
    public IcesiTokenDTO token(@RequestBody IcesiLoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));
        return IcesiTokenDTO.builder().token(tokenService.generateToken(authentication)).build();
    }

}
