package co.com.icesi.icesiAccountSystem.controller;

import co.com.icesi.icesiAccountSystem.security.IcesiSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class HomeController {
    @GetMapping("/home")
    public static String getCurrentRole() throws ParseException{
        return IcesiSecurityContext.getCurrentUserRole();
    }
}
