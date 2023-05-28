package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.AccountAPI;
import co.edu.icesi.demo.dto.AccountDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.edu.icesi.demo.api.AccountAPI.BASE_ACCOUNT_URL;

@RestController
@RequestMapping(BASE_ACCOUNT_URL)
@AllArgsConstructor
@CrossOrigin
public class AccountController implements AccountAPI {

    private final AccountService accountService;
    @Override
    public AccountDTO getAccount(String accountNumber) {

        return accountService.getAccount(accountNumber);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {

        return accountService.getAllAccounts();
    }

    @Override
    public AccountDTO addAccount(AccountDTO accountDTO) {

        return accountService.save(accountDTO);
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
    public AccountDTO enableAccount(String accountNumber) {

        return accountService.enableAccount(accountNumber);
    }

    @Override
    public AccountDTO disableAccount(String accountNumber) {

        return accountService.disableAccount(accountNumber);
    }
}
