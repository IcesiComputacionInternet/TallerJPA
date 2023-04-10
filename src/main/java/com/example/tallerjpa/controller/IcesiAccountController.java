package com.example.tallerjpa.controller;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.TransactionRequestDTO;
import com.example.tallerjpa.dto.TransactionResponseDTO;
import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.tallerjpa.api.AccountAPI.BASE_ACCOUNT_URL;

@RestController
@AllArgsConstructor
public class IcesiAccountController {

    private final AccountService accountService;
    @PostMapping("/accounts")
    public IcesiAccount createAccount(@RequestBody AccountDTO accountDTO){return accountService.createAccount(accountDTO);}

    @PatchMapping("/accounts/activate/{accountNumber}")
    public String activateAccount(@PathVariable String accountNumber){
        return accountService.activateAccount(accountNumber);
    }

    @PatchMapping("/accounts/deactivate/{accountNumber}")
    public String deactivateAccount(@PathVariable String accountNumber){
        return accountService.deactivateAccount(accountNumber);
    }

    @PatchMapping("/accounts/withdraw")
    public TransactionResponseDTO withdrawMoney(@RequestBody TransactionRequestDTO transactionRequestDTO){
        return accountService.withdrawMoney(transactionRequestDTO);
    }

    @PatchMapping("/accounts/deposit")
    public TransactionResponseDTO depositMoney(@RequestBody TransactionRequestDTO transactionRequestDTO){
        return accountService.depositMoney(transactionRequestDTO);
    }

    @PatchMapping("/accounts/transfer")
    public TransactionResponseDTO transferMoney(@RequestBody TransactionRequestDTO transactionRequestDTO){
        return accountService.transferMoney(transactionRequestDTO);
    }


}
