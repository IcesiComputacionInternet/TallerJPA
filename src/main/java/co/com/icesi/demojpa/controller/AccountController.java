package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.servicio.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class AccountController {

    private final AccountService accountService;

    private final AccountRepository accountRepository;


    public AccountController(AccountRepository accountRepository, AccountService accountService, AccountRepository accountRepository1) {
        this.accountService = accountService;
        this.accountRepository = accountRepository1;
    }

    @PostMapping("/accounts")
    public IcesiAccount createIcesiAccount(@RequestBody AccountCreateDTO accountCreateDTO){
        return accountService.save(accountCreateDTO);
    }

    @PostMapping("/disableAccount/{accountNumber}")
    public void disableAccount(@PathVariable String accountNumber){
        accountService.disableAccount(accountNumber);
    }

    @PostMapping ("/accounts/{id}")
    public Optional<IcesiAccount> showAccountsById(@PathVariable String id){
        return accountRepository.findById(UUID.fromString(id));
    }

    @GetMapping ("/all")
    public List<IcesiAccount> showAccounts(){
        return accountRepository.findAll();
    }

}
