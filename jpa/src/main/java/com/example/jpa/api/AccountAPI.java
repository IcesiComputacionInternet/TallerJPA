package com.example.jpa.api;

import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.AccountResponseDTO;
import com.example.jpa.dto.TransactionRequestDTO;
import com.example.jpa.dto.TransactionResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(AccountAPI.BASE_ACCOUNT_URL)
public interface AccountAPI {

    String BASE_ACCOUNT_URL = "/accounts";

    //Add the signature of the methods that are going to be implemented in the controller
    @PostMapping("/create")
    AccountResponseDTO createAccount(@RequestBody AccountRequestDTO accountRequestDTO);

    @GetMapping("/{accountID}")
    AccountResponseDTO getAccount(@PathVariable("accountID") String accountID);

    @GetMapping("/all")
    List<AccountResponseDTO> getAccount();

    @PatchMapping("/enable")
    String enableAccount(@RequestBody String accountNumber);

    @PatchMapping("/disable")
    String disableAccount(@RequestBody String accountNumber);

    @PatchMapping("/deposit")
    TransactionResponseDTO deposit(@RequestBody TransactionRequestDTO accountNumber);

    @PatchMapping("/withdraw")
    TransactionResponseDTO withdraw(@RequestBody TransactionRequestDTO accountNumber);

    @PatchMapping("/transfer")
    TransactionResponseDTO transfer(@RequestBody TransactionRequestDTO accountNumber);

}
