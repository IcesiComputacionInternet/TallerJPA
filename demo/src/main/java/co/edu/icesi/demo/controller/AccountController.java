package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.dto.IcesiAccountDto;
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
    public IcesiAccountDto save(@RequestBody IcesiAccountDto account){
        return icesiAccountService.save(account);
    }
}
