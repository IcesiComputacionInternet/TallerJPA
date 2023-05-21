package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.api.AccountAPI;
import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.TransactionOperationDTO;
import co.com.icesi.demojpa.dto.TransactionResultDTO;
import co.com.icesi.demojpa.service.AccountService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements AccountAPI {

    private AccountService accountService;

    @Override
    public AccountCreateDTO add(AccountCreateDTO account) {
        return null;
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
    public String enableAccount(String accountNumber) {
        return null;
    }

    @Override
    public String disableAccount(String accountNumber) {
        return null;
    }
}
