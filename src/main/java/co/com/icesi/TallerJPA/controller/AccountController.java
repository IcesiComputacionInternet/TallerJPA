package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.IcesiAccountAPI;
import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.dto.TransactionOperationDTO;
import co.com.icesi.TallerJPA.dto.response.AccountResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import co.com.icesi.TallerJPA.service.AccountService;

import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiAccountAPI.BASE_ACCOUNT_URL;

@RestController
@AllArgsConstructor
public class AccountController implements IcesiAccountAPI {
    private final AccountService accountService;

    @Override
    public AccountResponseDTO createIcesiAccount(AccountCreateDTO account) {
        return accountService.save(account);
    }

    @Override
    public String enableAccount(String accountNumber) {
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public String disableAccount(String accountNumber) {
        return accountService.disableAccount(accountNumber);
    }

    @Override
    public TransactionOperationDTO withdraw(TransactionOperationDTO transaction){
        return accountService.withdraw(transaction);
    }

    @Override
    public TransactionOperationDTO deposit(TransactionOperationDTO transaction) {
        return accountService.deposit(transaction);
    }

    @Override
    public TransactionOperationDTO transfer(TransactionOperationDTO transaction) {
        return accountService.transfer(transaction);
    }

    @Override
    public AccountResponseDTO getAccountByNumber(String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }



}
