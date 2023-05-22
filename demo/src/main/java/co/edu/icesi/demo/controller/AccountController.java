package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.AccountApi;
import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.dto.TransactionOperationDto;
import co.edu.icesi.demo.dto.TransactionResultDto;
import co.edu.icesi.demo.enums.PathRoutesDefault;
import co.edu.icesi.demo.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AccountController implements AccountApi {
    private final IcesiAccountService icesiAccountService;


    @Override
    public IcesiAccountDto save(IcesiAccountDto account) {
        return icesiAccountService.save(account);
    }

    @Override
    public TransactionResultDto withdraw(TransactionOperationDto transaction) {
        return icesiAccountService.withdraw(transaction);
    }

    @Override
    public TransactionResultDto deposit(TransactionOperationDto transaction) {
        return icesiAccountService.deposit(transaction);
    }

    @Override
    public TransactionResultDto transfer(TransactionOperationDto transaction) {
        return icesiAccountService.transfer(transaction);
    }

    @Override
    public String enableAccount(String accountNumber) {
        return icesiAccountService.enableAccount(accountNumber);
    }

    @Override
    public String disableAccount(String accountNumber) {
        return icesiAccountService.disableAccount(accountNumber);
    }
}
