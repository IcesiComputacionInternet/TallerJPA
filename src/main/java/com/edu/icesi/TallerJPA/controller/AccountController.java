package com.edu.icesi.TallerJPA.controller;


import com.edu.icesi.TallerJPA.api.AccountAPI;
import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.TransactionDTO;
import com.edu.icesi.TallerJPA.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.edu.icesi.TallerJPA.api.AccountAPI.BASE_ACCOUNT_URL;

@RequestMapping(BASE_ACCOUNT_URL)
@RestController
@AllArgsConstructor
public class AccountController implements AccountAPI {

    private final AccountService accountService;

    @Override
    public AccountCreateDTO getAccount(String accountNumber) {
        return null;
    }

    @Override
    public List<AccountCreateDTO> getAllUsers() {
        return null;
    }

    @Override
    public AccountCreateDTO addAccount(AccountCreateDTO accountCreateDTO) {
        return accountService.save(accountCreateDTO);
    }

    @Override
    public TransactionDTO withdraw(TransactionDTO transactionDTO) {
        return accountService.withdrawals(transactionDTO);
    }

    @Override
    public TransactionDTO deposit(TransactionDTO transactionDTO) {
        return accountService.depositMoney(transactionDTO);
    }

    @Override
    public TransactionDTO transfer(TransactionDTO transactionDTO) {
        return accountService.transferMoney(transactionDTO);
    }

    @Override
    public AccountCreateDTO enable(String accountNumber) {
        return accountService.setToEnableState(accountNumber);
    }

    @Override
    public AccountCreateDTO disable(String accountNumber) {
        return accountService.setToDisableState(accountNumber);
    }
}
