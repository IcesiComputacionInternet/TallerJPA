package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.api.AccountAPI;
import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.TransactionOperationDTO;
import co.com.icesi.demojpa.dto.TransactionResultDTO;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.security.IcesiSecurityContext;
import co.com.icesi.demojpa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class AccountController implements AccountAPI {

    @Autowired
    private AccountService accountService;

    @Override
    public AccountCreateDTO add(AccountCreateDTO account) {
        return null;
    }

    @CrossOrigin
    @Override
    public List<AccountCreateDTO> findAll() {

        return accountService.findAll();
    }

    @Override
    public TransactionResultDTO withdraw(@RequestBody TransactionOperationDTO transaction){
        return accountService.withdraw(transaction);
    }

    @Override
    public TransactionResultDTO deposit(@RequestBody TransactionOperationDTO transaction){
        return accountService.deposit(transaction);
    }

    @Override
    public TransactionResultDTO transfer(@RequestBody TransactionOperationDTO transaction){
        return accountService.transferMoney(transaction);
    }

    @Override
    public AccountCreateDTO enableAccount(String accountNumber) {
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public AccountCreateDTO disableAccount(String accountNumber) {
        return accountService.disableAccount(accountNumber);
    }
}
