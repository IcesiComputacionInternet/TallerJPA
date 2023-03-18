package com.icesi.TallerJPA.controller;

import com.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.IcesiAccountResponseDTO;
import com.icesi.TallerJPA.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class IcesiAccountController {

    private final AccountService accountService;
    @PostMapping("/account")
    public IcesiAccountResponseDTO createIcesiAccount(@RequestBody IcesiAccountDTO icesiAccountDTO){
        return  accountService.save(icesiAccountDTO);
    }

    @PatchMapping("/account/activeAccount/{accountNumber}")
    public String activeAccount(@PathVariable String accountNumber){
        return accountService.activeAccount(accountNumber);
    }

    @PatchMapping("/account/inactiveAccount/{accountNumber}")
    public String inactiveAccount(@PathVariable String accountNumber){
        return accountService.disableAccount(accountNumber);
    }

    @PatchMapping("/account/withdrawal/{accountNumber}")
    public String withdrawalAccount(@PathVariable String accountNumber, @RequestBody Long value){
        return accountService.withdrawal(accountNumber, value);
    }
}
