package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.AccountAPI;
import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.edu.icesi.demo.api.AccountAPI.BASE_ACCOUNT_URL;

@RestController
@RequestMapping(BASE_ACCOUNT_URL)
@AllArgsConstructor
public class AccountController implements AccountAPI {

    private final AccountService accountService;
    @Override
    public AccountCreateDTO getAccount(String accountNumber) {

        return null;
    }

    @Override
    public List<AccountCreateDTO> getAllAccounts() {

        return null;
    }

    @Override
    public AccountCreateDTO addAccount(AccountCreateDTO accountCreateDTO) {

        return accountService.save(accountCreateDTO);
    }

    @Override
    public TransactionDTO withdrawalMoney(TransactionDTO transactionDTO) {

        return accountService.withdrawalMoney(transactionDTO);
    }

    @Override
    public TransactionDTO depositMoney(TransactionDTO transactionDTO) {

        return accountService.depositMoney(transactionDTO);
    }

    @Override
    public TransactionDTO transferMoney(TransactionDTO transactionDTO) {

        return accountService.transferMoney(transactionDTO);
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
