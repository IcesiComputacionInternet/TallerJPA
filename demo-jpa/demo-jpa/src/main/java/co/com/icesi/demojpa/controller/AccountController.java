package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.api.AccountAPI;
import co.com.icesi.demojpa.dto.request.AccountCreateDTO;
import co.com.icesi.demojpa.dto.response.AccountResponseDTO;
import co.com.icesi.demojpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AccountController implements AccountAPI {

    private final AccountService accountService;

    @Override
    public AccountResponseDTO createIcesiAccount(@RequestBody AccountCreateDTO icesiAccountDTO){
        return  accountService.save(icesiAccountDTO);
    }

    @Override
    public  String activeAccount(@PathVariable String accountNumber){
        return accountService.activeAccount(accountNumber);
    }

    @Override
    public String inactiveAccount(@PathVariable String accountNumber){
        return accountService.disableAccount(accountNumber);
    }

    @Override
    public String withdrawalAccount(@PathVariable String accountNumber, @RequestBody Long value){
        return accountService.withdrawal(accountNumber, value);
    }

    @Override
    public String depositAccount(@PathVariable String accountNumber, @RequestBody Long value){
        return accountService.deposit(accountNumber, value);
    }

    @Override
    public String transferAccount(@PathVariable String accountNumberOrigin, @PathVariable String accountNumberDestination, @RequestBody Long value){
        return accountService.transfer(accountNumberOrigin, accountNumberDestination, value);
    }
}