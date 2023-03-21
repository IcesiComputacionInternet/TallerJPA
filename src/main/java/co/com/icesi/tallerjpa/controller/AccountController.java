package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.AccountApi;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController(AccountApi.BASE_URL)
@AllArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    public ResponseAccountDTO save(@RequestBody RequestAccountDTO account){
       return accountService.save(account);
    }

    public String withdraw(@RequestBody TransactionDTO transactionDTO){
        return accountService.withdraw(transactionDTO.getAmount(), transactionDTO.getAccountNumberOrigin());
    }

    public String deposit(@RequestBody TransactionDTO transactionDTO){
        return accountService.deposit(transactionDTO.getAmount(), transactionDTO.getAccountNumberOrigin());
    }

    public String transfer(@RequestBody TransactionDTO transactionDTO){
        return accountService.transfer(
                transactionDTO.getAmount(),
                transactionDTO.getAccountNumberOrigin(),
                transactionDTO.getAccountNumberDestination()
        );
    }

    public String enableAccount(@PathVariable String accountNumber){
        return accountService.enableAccount(accountNumber);
    }
    
    public String disableAccount(@PathVariable String accountNumber){
        return accountService.disableAccount(accountNumber);
    }
}
