package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.IcesiSecurityContext;
import com.nimbusds.jose.shaded.json.parser.ParseException;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() throws ParseException {
        return "Hello, " + IcesiSecurityContext.getCurrentUserId();
    }

    @GetMapping("/admin")
    public String admin() throws ParseException {
        return "Hello, admin " + IcesiSecurityContext.getCurrentUserId() + " " + IcesiSecurityContext.getCurrentUserRole();
    }

}
