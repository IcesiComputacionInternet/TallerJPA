package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/accounts/add")
    public IcesiAccount createIcesiAccount(@RequestBody AccountCreateDTO account){
        return accountService.save(account);
    }

}
