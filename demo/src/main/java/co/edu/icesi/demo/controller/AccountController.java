package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.dto.TransactionOperationDto;
import co.edu.icesi.demo.dto.TransactionResultDto;
import co.edu.icesi.demo.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AccountController  {
    private final IcesiAccountService icesiAccountService;

    @PostMapping("/add/account")
    public IcesiAccountDto save(@RequestBody IcesiAccountDto account){
        return icesiAccountService.save(account);
    }
    @PatchMapping("/withdraw/")
    public TransactionResultDto withdraw(@RequestBody TransactionOperationDto transaction){
        return icesiAccountService.withdraw(transaction);
    }

    @PatchMapping("/deposit/")
    public TransactionResultDto deposit(@RequestBody TransactionOperationDto transaction){
        return icesiAccountService.deposit(transaction);
    }

    @PatchMapping("/transfer/")
    public TransactionResultDto transfer(@RequestBody TransactionOperationDto transaction){
        return icesiAccountService.transfer(transaction);
    }

    @PatchMapping("/enableAccount/{accountNumber}")
    public String enableAccount(@PathVariable String accountNumber){
        return icesiAccountService.enableAccount(accountNumber);
    }

    @PatchMapping("/disableAccount/{accountNumber}")
    public String disableAccount(@PathVariable String accountNumber){
        return icesiAccountService.disableAccount(accountNumber);
    }

}
