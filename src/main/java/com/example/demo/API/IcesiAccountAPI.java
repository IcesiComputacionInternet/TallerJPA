package com.example.demo.API;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.DTO.ResponseTransactionDTO;
import com.example.demo.DTO.TransactionCreateDTO;


@RequestMapping("/accounts")
public interface IcesiAccountAPI {

    @PostMapping("/add")
    public ResponseIcesiAccountDTO create(@Valid @RequestBody IcesiAccountCreateDTO icesiAccountCreateDTO);

    @PostMapping("enable/{accountNumber}")
    public ResponseIcesiAccountDTO enableAccount(@Valid @RequestBody IcesiAccountCreateDTO icesiAccountCreateDTO);

    @PostMapping("disable/{accountNumber}")
    public ResponseIcesiAccountDTO disableAccount(@Valid @RequestBody IcesiAccountCreateDTO icesiAccountCreateDTO);

    @PostMapping("withdrawal")
    public ResponseTransactionDTO withdrawalMoney(@Valid @RequestBody TransactionCreateDTO transactionCreateDTO);

    @PostMapping("deposit")
    public ResponseTransactionDTO depositMoney(@Valid @RequestBody TransactionCreateDTO transactionCreateDTO);

    @PostMapping("transfer")
    public ResponseTransactionDTO transferMoneyToAnotherAccount(@Valid @RequestBody TransactionCreateDTO transactionCreateDTO);
    
}
