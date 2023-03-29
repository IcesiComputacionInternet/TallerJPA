package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.AccountApi;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AccountController implements AccountApi {
    private final AccountService accountService;

    @Override
    public ResponseAccountDTO add(@Valid @RequestBody RequestAccountDTO account) {
        return accountService.save(account);
    }

    @Override
    public TransactionDTO withdraw(@Valid @RequestBody TransactionDTO transactionDTO){
        return accountService.withdraw(transactionDTO);
    }

    @Override
    public TransactionDTO deposit(@Valid @RequestBody TransactionDTO transactionDTO){
        return accountService.deposit(transactionDTO);
    }

    @Override
    public TransactionDTO transfer(@Valid @RequestBody TransactionDTO transactionDTO){
        return accountService.transfer(transactionDTO);
    }

    @Override
    public TransactionDTO enableAccount(@PathVariable String accountNumber){
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public TransactionDTO disableAccount(@PathVariable String accountNumber){
        return accountService.disableAccount(accountNumber);
    }
}
