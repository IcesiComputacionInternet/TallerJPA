package com.icesi.TallerJPA.controller;

import com.icesi.TallerJPA.api.AccountAPI;
import com.icesi.TallerJPA.dto.request.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.response.IcesiAccountResponseDTO;
import com.icesi.TallerJPA.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class IcesiAccountController implements AccountAPI {

    private final AccountService accountService;

    @Override
    public IcesiAccountResponseDTO createIcesiAccount(@RequestBody IcesiAccountDTO icesiAccountDTO){
        return  accountService.save(icesiAccountDTO);
    }

    @Override
    public String activeAccount(@PathVariable String accountNumber){
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
