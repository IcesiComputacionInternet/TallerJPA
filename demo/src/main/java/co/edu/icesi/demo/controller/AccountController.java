package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.dto.RequestAccountDto;
import co.edu.icesi.demo.dto.ResponseAccountDto;
import co.edu.icesi.demo.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController  {
    private final IcesiAccountService icesiAccountService;

    @PostMapping("/add/account")
    public ResponseAccountDto save(@RequestBody RequestAccountDto account){
        return icesiAccountService.save(account);
    }
}
