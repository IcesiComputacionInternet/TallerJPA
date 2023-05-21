package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.API.IcesiAccountAPI;
import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.service.IcesiAccountService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class IcesiAccountController implements IcesiAccountAPI {
    
    private final IcesiAccountService icesiAccountService;

    @Override
    public ResponseIcesiAccountDTO create(@Valid IcesiAccountCreateDTO icesiAccountCreateDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public ResponseIcesiAccountDTO enableAccount(@Valid IcesiAccountCreateDTO icesiAccountCreateDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enableAccount'");
    }

    @Override
    public ResponseIcesiAccountDTO disableAccount(@Valid IcesiAccountCreateDTO icesiAccountCreateDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disableAccount'");
    }

    @Override
    public void withdrawalMoney(long amountToWithdraw, @Valid IcesiAccountCreateDTO accountCreateDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawalMoney'");
    }

    @Override
    public void depositMoney() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'depositMoney'");
    }

    @Override
    public void transferMoneyToAnotherAccount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transferMoneyToAnotherAccount'");
    }

}
