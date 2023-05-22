package com.example.jpa.controller;

import com.example.jpa.security.IcesiSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAdminController {

    @GetMapping("/admin")
    public String test(){
        return "Hello " + IcesiSecurityContext.getCurrentUserId();
    }

}
