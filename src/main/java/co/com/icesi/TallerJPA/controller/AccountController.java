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
    public AccountResponseDTO getAccountByNumber(String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @Override
    public AccountResponseDTO createIcesiAccount(@RequestBody AccountCreateDTO account) {
        return accountService.save(account);
    }

    @Override
    public String enableAccount(@PathVariable String accountNumber) {
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public String disableAccount(@PathVariable String accountNumber) {
        return accountService.disableAccount(accountNumber);
    }

    @Override
    public TransactionOperationDTO withdraw(@RequestBody TransactionOperationDTO transaction){
        return accountService.withdraw(transaction);
    }

    @Override
    public TransactionOperationDTO deposit(@RequestBody TransactionOperationDTO transaction) {
        return accountService.deposit(transaction);
    }

    @Override
    public TransactionOperationDTO transfer(@RequestBody TransactionOperationDTO transaction) {
        return accountService.transfer(transaction);
    }


    /*
    Me falta
    lo del DTO de las operaciones
    revisar el metodo de transferencia


     */




}
