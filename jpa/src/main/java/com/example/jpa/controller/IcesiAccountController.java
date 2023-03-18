package com.example.jpa.controller;

import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.AccountResponseDTO;
import com.example.jpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class IcesiAccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.save(accountRequestDTO);
    }

    @GetMapping
    public List<AccountResponseDTO> getAccount() {
        return accountService.getAccounts();
    }

    @PatchMapping("/enable")
    public String enableAccount(@RequestBody String accountNumber) {
        return accountService.enableAccount(accountNumber) ? "Account enabled" : "Account not found";
    }

    @PatchMapping("/disable")
    public String disableAccount(@RequestBody String accountNumber) {
        return accountService.disableAccount(accountNumber) ? "Account disabled" : "Account not found";
    }

    @PatchMapping("/deposit")
    public String deposit(@RequestBody String accountNumber, @RequestBody String amount) {
        return accountService.deposit(accountNumber, Long.parseLong(amount)) ? "Deposit successful" : "Account not found";
    }

    @PatchMapping("/withdraw")
    public String withdraw(@RequestBody String accountNumber, @RequestBody String amount) {
        return accountService.withdraw(accountNumber, Long.parseLong(amount)) ? "Withdraw successful" : "Account not found";
    }

    @PatchMapping("/transfer")
    public String transfer(@RequestBody String accountNumberFrom, @RequestBody String accountNumberTo, @RequestBody String amount) {
        return accountService.transfer(accountNumberFrom, accountNumberTo, Long.parseLong(amount)) ? "Transfer successful" : "Account not found";
    }
}
