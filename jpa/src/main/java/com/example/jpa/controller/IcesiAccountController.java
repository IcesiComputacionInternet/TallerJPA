package com.example.jpa.controller;

import com.example.jpa.api.AccountAPI;
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
@RequestMapping(AccountAPI.BASE_ACCOUNT_URL)
public class IcesiAccountController implements AccountAPI {

    private final AccountService accountService;

    @Override
    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.save(accountRequestDTO);
    }

    @Override
    public AccountResponseDTO getAccount(String accountID) {
        return accountService.getAccountByAccountNumber(accountID);
    }

    @Override
    public List<AccountResponseDTO> getAccount() {
        return accountService.getAccounts();
    }

    @Override
    public String enableAccount(String accountNumber) {
        return accountService.enableAccount(accountNumber) ? "Account enabled" : "Account not found";
    }

    @Override
    public String disableAccount(String accountNumber) {
        return accountService.disableAccount(accountNumber) ? "Account disabled" : "Account not found";
    }

    @Override
    public TransactionResponseDTO deposit(TransactionRequestDTO transactionRequestDTO) {
        return accountService.deposit(transactionRequestDTO);
    }

    @Override
    public TransactionResponseDTO withdraw(TransactionRequestDTO transactionRequestDTO) {
        return accountService.withdraw(transactionRequestDTO);
    }

    @Override
    public TransactionResponseDTO transfer(TransactionRequestDTO transactionRequestDTO) {
        return accountService.transfer(transactionRequestDTO);
    }
}
