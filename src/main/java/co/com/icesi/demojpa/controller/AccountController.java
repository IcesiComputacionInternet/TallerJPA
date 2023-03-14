package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.repository.AccountRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private AccountRepository accountRepository;


    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
