package co.com.icesi.tallerjpa.controller;


import co.com.icesi.tallerjpa.security.IcesiSecurityContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.ParseException;
@RestController
public class HomeController {

    @PostMapping("/home")
    public String home() throws ParseException {
        return IcesiSecurityContext.getCurrentRole();
    }

}
