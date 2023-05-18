package com.example.demo.API;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;


@RequestMapping("/accounts")
public interface IcesiAccountAPI {

    @PostMapping("/add")
    ResponseIcesiAccountDTO create(@Valid @RequestBody IcesiAccountCreateDTO icesiAccountCreateDTO);


    @PostMapping("")
    ResponseIcesiAccountDTO enableAccount(@Valid @RequestBody IcesiAccountCreateDTO icesiAccountCreateDTO);

    @PostMapping("")
    ResponseIcesiAccountDTO disableAccount(@Valid @RequestBody IcesiAccountCreateDTO icesiAccountCreateDTO);

    @PostMapping
    void withdrawalMoney(long amountToWithdraw, @Valid @RequestBody IcesiAccountCreateDTO accountCreateDTO);

    @PostMapping
    void depositMoney();

    @PostMapping
    void transferMoneyToAnotherAccount();
    
}
