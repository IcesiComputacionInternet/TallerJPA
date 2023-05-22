package com.example.demo.controller;

import com.example.demo.DTO.*;
import com.example.demo.api.IcesiAccountApi;
import com.example.demo.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@Validated
public class IcesiAccountController implements IcesiAccountApi {
    private final IcesiAccountService icesiAccountService;
    @Override
    public IcesiAccountDTO save(@RequestBody @Valid IcesiAccountCreateDTO account) {
        return icesiAccountService.save();
    }

    @Override
    public IcesiAccountDTO withdraw(@RequestBody @Valid TransactionAccountDTO transaction) {
        return null;
    }

    @Override
    public IcesiAccountDTO deposit(@RequestBody @Valid TransactionAccountDTO transaction) {
        return null;
    }

    @Override
    public TransferResponseDTO transfer(@RequestBody @Valid TransferRequestDTO transaction) {
        return null;
    }

    @Override
    public IcesiAccountDTO enableAccount(@PathVariable String accountNumber) {
        return null;
    }

    @Override
    public IcesiAccountDTO disableAccount(@PathVariable String accountNumber) {
        return null;
    }

    @Override
    public void testPath(@RequestBody @Valid TransferRequestDTO transaction) {

    }
}
