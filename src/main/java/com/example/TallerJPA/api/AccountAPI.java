package com.example.TallerJPA.api;

import com.example.TallerJPA.dto.*;
import org.springframework.web.bind.annotation.*;

@RequestMapping(AccountAPI.BASE_ACCOUNT_URL)
public interface AccountAPI {
    String BASE_ACCOUNT_URL = "/accounts";
    @GetMapping("/getAccounts")
    public AccountListResponseDTO getAccounts();

    @PostMapping("/add/account")
    public AccountResponseDTO save(@RequestBody AccountCreateDTO account);
    @PatchMapping("/withdraw/")
    public AccountResponseDTO withdraw(@RequestBody TransactionAccountDTO transaction);

    @PatchMapping("/deposit/")
    public AccountResponseDTO deposit(@RequestBody TransactionAccountDTO transaction);
    @PatchMapping("/transfer/")
    public TransferResponseDTO transfer(@RequestBody TransferRequestDTO transaction);
    @PatchMapping("/enableAccount/{accountNumber}")
    public AccountResponseDTO enableAccount(@PathVariable String accountNumber);
    @PatchMapping("/disableAccount/{accountNumber}")
    public AccountResponseDTO disableAccount(@PathVariable String accountNumber);
    @PatchMapping("/testPath")
    public void testPath(@RequestBody TransferRequestDTO transaction);

}
