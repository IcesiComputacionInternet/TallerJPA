package com.example.tallerjpa.controller;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.service.AccountService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class IcesiAccountController {

    private final AccountService accountService;
    @PostMapping("/account")
    public IcesiAccount createAccount(@RequestBody AccountDTO accountDTO){return accountService.saveAccount(accountDTO);}

    @PatchMapping("/account/activate/{accountNumber}")
    public String activateAccount(@PathVariable String accountNumber){
        return accountService.activateAccount(accountNumber);
    }

    @PatchMapping("/account/deactivate/{accountNumber}")
    public String deactivateAccount(@PathVariable String accountNumber){
        return accountService.deactivateAccount(accountNumber);
    }

    @PatchMapping("/account/withdraw/{accountNumber}")
    public String withdrawMoney(@PathVariable String accountNumber, @RequestBody Long amount){
        return accountService.withdrawMoney(accountNumber, amount);
    }

    @PatchMapping("/account/deposit/{accountNumber}")
    public String depositMoney(@PathVariable String accountNumber, @RequestBody Long amount){
        return accountService.depositMoney(accountNumber, amount);
    }

    @PatchMapping("/account/transfer/{accountOrigin}/{accountDestination}")
    public String transferMoney(@PathVariable String accountOrigin, @PathVariable String accountDestination, @RequestBody Long amount){
        return accountService.transferMoney(accountOrigin, accountDestination, amount);
    }


}
