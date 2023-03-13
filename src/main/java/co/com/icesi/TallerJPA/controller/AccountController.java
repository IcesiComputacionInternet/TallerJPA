package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.dto.AccountResponseDTO;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import co.com.icesi.TallerJPA.service.AccountService;

@RestController
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/addAccount")
    public AccountResponseDTO createIcesiAccount(@RequestBody AccountCreateDTO account) {
        return accountService.save(account);
    }

    @PatchMapping("/changeState/{accountNumber}")
    public String changeState(@PathVariable String accountNumber) {
        return accountService.changeState(accountNumber);
    }

    @PatchMapping("/withdraw/{accountNumber}")
    public String withdraw(@PathVariable String accountNumber, @RequestBody Long amount) {
        return accountService.withdraw(accountNumber, amount);
    }

    @PatchMapping("/deposit/{accountNumber}")
    public String deposit(@PathVariable String accountNumber, @RequestBody Long amount) {
        return accountService.deposit(accountNumber, amount);
    }

    @PatchMapping("/transfer/{accountNumberOrigin}/{accountNumberDestination}")
    public String transfer(@PathVariable String accountNumberOrigin, @PathVariable String accountNumberDestination, @RequestBody Long amount) {
        return accountService.transferMoney(accountNumberOrigin, accountNumberDestination, amount);
    }




}
