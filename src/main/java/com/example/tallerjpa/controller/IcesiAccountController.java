package com.example.tallerjpa.controller;

import com.example.tallerjpa.api.AccountAPI;
import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.AccountResponseDTO;
import com.example.tallerjpa.dto.TransactionRequestDTO;
import com.example.tallerjpa.dto.TransactionResponseDTO;
import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.security.IcesiSecurityContext;
import com.example.tallerjpa.service.AccountService;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


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

    @Override
    public List<AccountResponseDTO> getAccountsByUser(){
        var userId = IcesiSecurityContext.getCurrentUserId();
        return accountService.getAllAccountsByUser(UUID.fromString(userId));
    }


}
