package com.example.tallerjpa.controller;

import com.example.tallerjpa.api.AccountAPI;
import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.TransactionRequestDTO;
import com.example.tallerjpa.dto.TransactionResponseDTO;
import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class IcesiAccountController implements AccountAPI {

    private final AccountService accountService;
    @Override
    public IcesiAccount createAccount(@RequestBody AccountDTO accountDTO){return accountService.createAccount(accountDTO);}

    @Override
    public String activateAccount(@PathVariable String accountNumber){
        return accountService.activateAccount(accountNumber);
    }

    @Override
    public String deactivateAccount(@PathVariable String accountNumber){
        return accountService.deactivateAccount(accountNumber);
    }

    @Override
    public TransactionResponseDTO withdrawMoney(@RequestBody TransactionRequestDTO transactionRequestDTO){
        return accountService.withdrawMoney(transactionRequestDTO);
    }

    @Override
    public TransactionResponseDTO depositMoney(@RequestBody TransactionRequestDTO transactionRequestDTO){
        return accountService.depositMoney(transactionRequestDTO);
    }

    @Override
    public TransactionResponseDTO transferMoney(@RequestBody TransactionRequestDTO transactionRequestDTO){
        return accountService.transferMoney(transactionRequestDTO);
    }


}
