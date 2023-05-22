package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.AccountApi;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.security.SecurityContext;
import co.com.icesi.tallerjpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@AllArgsConstructor
public class AccountController implements AccountApi {
    private final AccountService accountService;

    @Override
    public ResponseAccountDTO add(RequestAccountDTO account) {
        return accountService.save(account);
    }

    @Override
    public TransactionDTO withdraw(TransactionDTO transactionDTO){
        return accountService.withdraw(transactionDTO);
    }

    @Override
    public TransactionDTO deposit(TransactionDTO transactionDTO){
        return accountService.deposit(transactionDTO);
    }

    @Override
    public TransactionDTO transfer(TransactionDTO transactionDTO){
        return accountService.transfer(transactionDTO);
    }

    @Override
    public TransactionDTO enableAccount(String accountNumber){
        var userId = SecurityContext.getCurrentUserId();
        return accountService.enableAccount(accountNumber, UUID.fromString(userId));
    }

    @Override
    public TransactionDTO disableAccount(String accountNumber){
        var userId = SecurityContext.getCurrentUserId();
        return accountService.disableAccount(accountNumber, UUID.fromString(userId));
    }
}
