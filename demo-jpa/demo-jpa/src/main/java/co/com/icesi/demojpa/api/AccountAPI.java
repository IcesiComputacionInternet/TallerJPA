package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.request.AccountCreateDTO;
import co.com.icesi.demojpa.dto.response.AccountResponseDTO;
import org.springframework.web.bind.annotation.*;

@RequestMapping(AccountAPI.ACCOUNT_BASE_URL)
public interface AccountAPI {

    String ACCOUNT_BASE_URL = "/account";

    @PostMapping
    AccountResponseDTO createIcesiAccount(@RequestBody AccountCreateDTO icesiAccountDTO);

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

