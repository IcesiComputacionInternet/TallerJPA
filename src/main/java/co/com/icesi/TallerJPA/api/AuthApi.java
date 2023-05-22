package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.JwtResponse;
import co.com.icesi.TallerJPA.dto.LoginDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/auth")
public interface AuthApi {
    @PostMapping("/login")
    JwtResponse token(@Valid @RequestBody LoginDto loginDto);
}
