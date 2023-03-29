package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.AccountApi;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AccountApi.ACCOUNT_BASE_URL)
@AllArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public ResponseAccountDTO add(RequestAccountDTO account) {
        return accountService.save(account);
    }

    @Override
    public String withdraw(@RequestBody TransactionDTO transactionDTO){
        return accountService.withdraw(
                transactionDTO.getAmount(),
                transactionDTO.getAccountNumberOrigin()
        );
    }

    @Override
    public String deposit(@RequestBody TransactionDTO transactionDTO){
        return accountService.deposit(
                transactionDTO.getAmount(),
                transactionDTO.getAccountNumberOrigin()
        );
    }

    @Override
    public String transfer(@RequestBody TransactionDTO transactionDTO){
        return accountService.transfer(
                transactionDTO.getAmount(),
                transactionDTO.getAccountNumberOrigin(),
                transactionDTO.getAccountNumberDestination()
        );
    }

    @Override
    public String enableAccount(@PathVariable String accountNumber){
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public String disableAccount(@PathVariable String accountNumber){
        return accountService.disableAccount(accountNumber);
    }
}
