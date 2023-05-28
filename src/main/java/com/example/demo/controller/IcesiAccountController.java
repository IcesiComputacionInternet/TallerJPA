package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.API.IcesiAccountAPI;
import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.DTO.ResponseTransactionDTO;
import com.example.demo.DTO.TransactionCreateDTO;
import com.example.demo.security.IcesiSecurityContext;
import com.example.demo.service.IcesiAccountService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class IcesiAccountController implements IcesiAccountAPI {
    
    private final IcesiAccountService icesiAccountService;

    @Override
    public ResponseIcesiAccountDTO create(@Valid IcesiAccountCreateDTO icesiAccountCreateDTO) {
        return icesiAccountService.create(icesiAccountCreateDTO);
    }

    @Override
    public ResponseIcesiAccountDTO enableAccount(@Valid IcesiAccountCreateDTO icesiAccountCreateDTO) {
        return icesiAccountService.enableAccount(icesiAccountCreateDTO);
    }

    @Override
    public ResponseIcesiAccountDTO disableAccount(@Valid IcesiAccountCreateDTO icesiAccountCreateDTO) {
        return icesiAccountService.disableAccount(icesiAccountCreateDTO);
    }

    @Override
    public ResponseTransactionDTO withdrawalMoney(@Valid TransactionCreateDTO transactionCreateDTO) {
        return icesiAccountService.withdrawalMoney(transactionCreateDTO);
    }

    @Override
    public ResponseTransactionDTO depositMoney(@Valid TransactionCreateDTO transactionCreateDTO) {
        return icesiAccountService.depositMoney(transactionCreateDTO);
    }

    @Override
    public ResponseTransactionDTO transferMoneyToAnotherAccount(@Valid TransactionCreateDTO transactionCreateDTO) {
        return icesiAccountService.transferMoneyToAnotherAccount(transactionCreateDTO);
    }

    @Override
    public List<ResponseIcesiAccountDTO> getAccountByUser() {
        return icesiAccountService.findAccountsOwnedByAUser(IcesiSecurityContext.getCurrentUserId());
    }

}
