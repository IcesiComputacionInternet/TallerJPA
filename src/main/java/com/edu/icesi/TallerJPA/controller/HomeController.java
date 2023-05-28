package com.edu.icesi.TallerJPA.controller;

import com.edu.icesi.TallerJPA.security.IcesiSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class HomeController {

    @PostMapping("/home")
    public String getCurrentRol() throws ParseException {
        return IcesiSecurityContext.getCurrentRol();
    }
}
