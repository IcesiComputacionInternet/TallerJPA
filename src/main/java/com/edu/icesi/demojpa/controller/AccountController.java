package com.edu.icesi.demojpa.controller;

import com.edu.icesi.demojpa.api.AccountAPI;
import com.edu.icesi.demojpa.dto.request.RequestAccountDTO;
import com.edu.icesi.demojpa.dto.request.RequestTransactionDTO;
import com.edu.icesi.demojpa.dto.response.ResponseAccountDTO;
import com.edu.icesi.demojpa.dto.response.ResponseTransactionDTO;
import com.edu.icesi.demojpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.edu.icesi.demojpa.api.AccountAPI.BASE_ACCOUNT_URL;

@RestController
@RequestMapping(BASE_ACCOUNT_URL)
@AllArgsConstructor
public class AccountController implements AccountAPI {
    private final AccountService accountService;
    @Override
    public ResponseAccountDTO getAccount(String accountNumber) {
        return accountService.getAccount(accountNumber);
    }

    @Override
    public List<ResponseAccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @Override
    public ResponseAccountDTO createAccount(RequestAccountDTO requestAccountDTO) {
        return accountService.save(requestAccountDTO);
    }

    @Override
    public ResponseAccountDTO enableAccount(RequestAccountDTO account) {
        return accountService.enableAccount(account);
    }

    @Override
    public ResponseAccountDTO disableAccount(RequestAccountDTO account) {
        return accountService.disableAccount(account);
    }

    @Override
    public ResponseTransactionDTO withdraw(RequestTransactionDTO transaction) {
        return accountService.withdraw(transaction);
    }

    @Override
    public ResponseTransactionDTO deposit(RequestTransactionDTO transaction) {
        return accountService.deposit(transaction);
    }

    @Override
    public ResponseTransactionDTO transfer(RequestTransactionDTO transaction) {
        return accountService.transfer(transaction);
    }
}
