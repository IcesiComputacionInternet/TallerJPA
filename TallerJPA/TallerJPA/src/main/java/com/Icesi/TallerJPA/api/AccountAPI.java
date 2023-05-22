package com.Icesi.TallerJPA.api;

import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RequestMapping(AccountAPI.BASE_ACCOUNT_URL)
public interface AccountAPI {
    String BASE_ACCOUNT_URL = "/accounts";

    @PostMapping
    IcesiAccountDTO createIcesiAccount(@RequestBody @Valid IcesiAccountDTO icesiAccountDTO);

    @PatchMapping("/activeAccount/{accountNumber}")
    String activeAccount(@PathVariable String accountNumber);

    @PatchMapping("/inactiveAccount/{accountNumber}")
    String inactiveAccount(@PathVariable String accountNumber);

    @PatchMapping("/withdrawal/{accountNumber}")
    String withdrawalAccount(@PathVariable String accountNumber, @RequestBody Long value);

    @PatchMapping("/deposit/{accountNumber}")
    String depositAccount(@PathVariable String accountNumber, @RequestBody Long value);

    @PatchMapping("/transfer/{accountNumberOrigin}/{accountNumberDestination}")
    String transferAccount(@PathVariable String accountNumberOrigin, @PathVariable String accountNumberDestination, @RequestBody Long value);
}