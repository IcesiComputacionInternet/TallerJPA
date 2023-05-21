package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.api.AccountAPI;
import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseAccountDTO;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.servicio.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class AccountController implements AccountAPI {

    private final AccountService accountService;

    private final AccountRepository accountRepository;


    public AccountController(AccountRepository accountRepository, AccountService accountService, AccountRepository accountRepository1) {
        this.accountService = accountService;
        this.accountRepository = accountRepository1;
    }

    @Override
    public ResponseAccountDTO createIcesiAccount(AccountCreateDTO accountCreateDTO){
        return accountService.save(accountCreateDTO);
    }

    @Override
    public void disableAccount(String accountNumber){
        accountService.disableAccount(accountNumber);
    }


    @Override
    public void eneableAccount(String accountNumber){
        accountService.enableAccount(accountNumber);
    }

    @Override
    public void withdraw(String accountNumber, long amount){
        accountService.withdrawal(accountNumber, amount);
    }

    @Override
    public void deposit(String accountNumber, long amount){
        accountService.deposit(accountNumber, amount);
    }


    @Override
    public void transfer(String accountNumberOrigin, String accountNumberDestination, long amount){
        accountService.transfer(accountNumberOrigin, accountNumberDestination, amount);
    }
}
