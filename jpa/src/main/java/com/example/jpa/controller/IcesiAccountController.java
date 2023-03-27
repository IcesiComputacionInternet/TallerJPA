package com.example.jpa.controller;

import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.AccountResponseDTO;
import com.example.jpa.dto.TransactionRequestDTO;
import com.example.jpa.dto.TransactionResponseDTO;
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
    public TransactionResponseDTO deposit(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        return accountService.deposit(transactionRequestDTO);
    }

    @PatchMapping("/withdraw")
    public TransactionResponseDTO withdraw(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        return accountService.withdraw(transactionRequestDTO);
    }

    @PatchMapping("/transfer")
    public TransactionResponseDTO transfer(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        return accountService.transfer(transactionRequestDTO);
    }
}
