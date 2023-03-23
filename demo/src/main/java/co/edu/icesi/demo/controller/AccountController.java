package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.AccountAPI;
import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.edu.icesi.demo.api.AccountAPI.BASE_ACCOUNT_URL;

@RestController(BASE_ACCOUNT_URL)
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
        return null;
    }

    @Override
    public TransactionDTO withdrawalMoney(TransactionDTO transactionDTO) {
        return null;
    }

    @Override
    public TransactionDTO depositMoney(TransactionDTO transactionDTO) {
        return null;
    }

    @Override
    public TransactionDTO transferMoney(TransactionDTO transactionDTO) {
        return null;
    }

    @Override
    public AccountCreateDTO enableAccount(String accountNumber) {
        return null;
    }

    @Override
    public AccountCreateDTO disableAccount(String accountNumber) {
        return null;
    }
}
