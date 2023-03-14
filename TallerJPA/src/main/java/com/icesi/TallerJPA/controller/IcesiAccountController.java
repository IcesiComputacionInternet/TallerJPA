package com.icesi.TallerJPA.controller;

import com.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.icesi.TallerJPA.model.IcesiAccount;
import com.icesi.TallerJPA.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiAccountController {

    private final AccountService accountService;
    @PostMapping("/account")
    public IcesiAccount createIcesiAccount(@RequestBody IcesiAccountDTO icesiAccountDTO){
        return  accountService.save(icesiAccountDTO);
    }
}
