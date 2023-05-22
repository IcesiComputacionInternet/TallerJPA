package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.security.IcesiSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

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
