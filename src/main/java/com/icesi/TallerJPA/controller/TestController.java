package com.icesi.TallerJPA.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

    @GetMapping("/admin")
    public String testAdmin(){
        return "testAdmin";
    }

    @GetMapping("/user")
    public String testUser(){
        return "testUser";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

}
