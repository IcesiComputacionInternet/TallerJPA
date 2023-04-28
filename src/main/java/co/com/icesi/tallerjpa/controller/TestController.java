package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.security.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


   @GetMapping("/admin")
   public String test(){
       return "Hello " + SecurityContext.getCurrentUserId();
   }
}
