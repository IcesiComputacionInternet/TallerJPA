package co.com.icesi.icesiAccountSystem.controller;

import co.com.icesi.icesiAccountSystem.api.AccountAPI;
import co.com.icesi.icesiAccountSystem.dto.RequestAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.TransactionOperationDTO;
import co.com.icesi.icesiAccountSystem.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
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
        return accountService.saveAccount(requestAccountDTO);
    }

    @Override
    public ResponseAccountDTO enableAccount(String accountNumber) {
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public ResponseAccountDTO disableAccount(String accountNumber) {
        return accountService.disableAccount(accountNumber);
    }

    @Override
    public TransactionOperationDTO withdrawMoney(TransactionOperationDTO transaction) {
        return accountService.withdrawMoney(transaction);
    }

    @Override
    public TransactionOperationDTO depositMoney(TransactionOperationDTO transaction) {
        return accountService.depositMoney(transaction);
    }

    @Override
    public TransactionOperationDTO transferMoney(TransactionOperationDTO transaction) {
        return accountService.transferMoney(transaction);
    }
}
