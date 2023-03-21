package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionOperationDTO;
import co.com.icesi.tallerjpa.dto.TransactionResultDTO;
import co.com.icesi.tallerjpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/add/account")
    public ResponseAccountDTO save(@RequestBody RequestAccountDTO account){
       return accountService.save(account);
    }

    @PatchMapping("/withdraw/")
    public TransactionResultDTO withdraw(@RequestBody TransactionOperationDTO transaction){
        return accountService.withdraw(transaction);
    }

    @PatchMapping("/deposit/")
    public TransactionResultDTO deposit(@RequestBody TransactionOperationDTO transaction){
        return accountService.deposit(transaction);
    }

    @PatchMapping("/transfer/")
    public TransactionResultDTO transfer(@RequestBody TransactionOperationDTO transaction){
        return accountService.transfer(transaction);
    }

    @PatchMapping("/enableAccount/{accountNumber}")
    public String enableAccount(@PathVariable String accountNumber){
        return accountService.enableAccount(accountNumber);
    }

    @PatchMapping("/disableAccount/{accountNumber}")
    public String disableAccount(@PathVariable String accountNumber){
        return accountService.disableAccount(accountNumber);
    }
}
