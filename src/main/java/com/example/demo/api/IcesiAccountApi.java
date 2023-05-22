package com.example.demo.api;

import com.example.demo.DTO.*;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/accounts")
public interface IcesiAccountApi {
    @PostMapping("/add/account")
    public IcesiAccountDTO save(@RequestBody IcesiAccountCreateDTO account);
    @PatchMapping("/withdraw/")
    public IcesiAccountDTO withdraw(@RequestBody TransactionAccountDTO transaction);

    @PatchMapping("/deposit/")
    public IcesiAccountDTO deposit(@RequestBody TransactionAccountDTO transaction);
    @PatchMapping("/transfer/")
    public TransferResponseDTO transfer(@RequestBody TransferRequestDTO transaction);
    @PatchMapping("/enableAccount/{accountNumber}")
    public IcesiAccountDTO enableAccount(@PathVariable String accountNumber);
    @PatchMapping("/disableAccount/{accountNumber}")
    public IcesiAccountDTO disableAccount(@PathVariable String accountNumber);
    @PatchMapping("/testPath")
    public void testPath(@RequestBody TransferRequestDTO transaction);

}