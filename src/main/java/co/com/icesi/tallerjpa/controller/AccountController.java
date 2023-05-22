package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.api.AccountAPI;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static co.com.icesi.tallerjpa.api.AccountAPI.ACCOUNT_URL;

@RestController
@RequestMapping(ACCOUNT_URL)
@AllArgsConstructor
public class AccountController implements AccountAPI {
    private final AccountService accountService;

    @Override
    public ResponseAccountDTO addAccount(@Valid RequestAccountDTO accountDTO) {
        return accountService.save(accountDTO);
    }

    @Override
    public ResponseAccountDTO enableAcc(String accNum) {
        return accountService.enableAccount(accNum);
    }

    @Override
    public ResponseAccountDTO disableAcc(String accNum) {
        return accountService.disableAccount(accNum);
    }

    @Override
    public TransactionDTO withdrawal(TransactionDTO transacDTO) {
        return accountService.withdrawal(transacDTO);
    }

    @Override
    public TransactionDTO deposit(TransactionDTO transacDTO) {
        return accountService.deposit(transacDTO);
    }

    @Override
    public TransactionDTO transfer(TransactionDTO transacDTO) {
        return accountService.transfer(transacDTO);
    }
}
