package com.example.TallerJPA.controller;

import com.example.TallerJPA.api.AccountAPI;
import com.example.TallerJPA.dto.*;
import com.example.TallerJPA.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.TallerJPA.api.AccountAPI.BASE_ACCOUNT_URL;

@AllArgsConstructor
@RestController
public class AccountController implements AccountAPI {
    private final AccountService accountService;
    @Override
    public AccountResponseDTO save(@RequestBody AccountCreateDTO account){
        return accountService.save(account);
    }

    @Override
    public AccountResponseDTO withdraw(@RequestBody TransactionAccountDTO transaction){
        return accountService.withdraw(transaction);
    }

    @Override
    public AccountResponseDTO deposit(@RequestBody TransactionAccountDTO transaction){
        return accountService.deposit(transaction);
    }

    @Override
    public TransferResponseDTO transfer(@RequestBody TransferRequestDTO transaction){
        return accountService.transfer(transaction);
    }

    @Override
    public AccountResponseDTO enableAccount(@PathVariable String accountNumber){
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public AccountResponseDTO disableAccount(@PathVariable String accountNumber){
        return accountService.disableAccount(accountNumber);
    }

}
