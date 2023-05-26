package com.example.tallerjpa.api;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.TransactionRequestDTO;
import com.example.tallerjpa.dto.TransactionResponseDTO;
import com.example.tallerjpa.model.IcesiAccount;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RequestMapping(AccountAPI.BASE_ACCOUNT_URL)
public interface AccountAPI {

    String BASE_ACCOUNT_URL = "/accounts";
    @PostMapping
    IcesiAccount createAccount(AccountDTO accountDTO);

    @PatchMapping("/activate/{accountNumber}")
    String activateAccount(@PathVariable String accountNumber);

    @PatchMapping("/deactivate/{accountNumber}")
    String deactivateAccount(@PathVariable String accountNumber);

    @PatchMapping("/withdraw/")
    TransactionResponseDTO withdrawMoney(@RequestBody TransactionRequestDTO transactionRequestDTO);

    @PatchMapping("/deposit/")
    TransactionResponseDTO depositMoney(@RequestBody TransactionRequestDTO transactionRequestDTO);

    @PatchMapping("/transfer/")
    TransactionResponseDTO transferMoney(@RequestBody TransactionRequestDTO transactionRequestDTO);











}
