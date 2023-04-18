package com.icesi.TallerJPA.api;

import com.icesi.TallerJPA.dto.request.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.response.IcesiAccountResponseDTO;
import org.springframework.web.bind.annotation.*;

@RequestMapping(AccountAPI.ACCOUNT_BASE_URL)
public interface AccountAPI {

    String ACCOUNT_BASE_URL = "/account";


    @PostMapping
    IcesiAccountResponseDTO createIcesiAccount(@RequestBody IcesiAccountDTO icesiAccountDTO);
    /*

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

    @PatchMapping("/account/deposit/{accountNumber}")
    public String depositAccount(@PathVariable String accountNumber, @RequestBody Long value){
        return accountService.deposit(accountNumber, value);
    }

    @PatchMapping("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}")
    public String transferAccount(@PathVariable String accountNumberOrigin, @PathVariable String accountNumberDestination, @RequestBody Long value){
        return accountService.transfer(accountNumberOrigin, accountNumberDestination, value);
    }

*/

}
