package com.icesi.TallerJPA.api;

import com.icesi.TallerJPA.dto.request.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.response.IcesiAccountResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(AccountAPI.ACCOUNT_BASE_URL)
public interface AccountAPI {

    String ACCOUNT_BASE_URL = "/account";

    @PostMapping
    IcesiAccountResponseDTO createIcesiAccount(@RequestBody IcesiAccountDTO icesiAccountDTO);

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

    @GetMapping("/getAccounts")
    List<IcesiAccountResponseDTO> getAccountsOfUsers();
}
