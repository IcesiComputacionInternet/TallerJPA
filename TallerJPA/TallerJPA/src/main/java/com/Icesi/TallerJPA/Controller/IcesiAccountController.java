package com.Icesi.TallerJPA.Controller;

import com.Icesi.TallerJPA.api.AccountAPI;
import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IcesiAccountController implements AccountAPI {
    private final IcesiAccountService IcesiAccountService;

    @Override
    public IcesiAccountDTO createIcesiAccount(IcesiAccountDTO icesiAccountDTO) {
        return IcesiAccountService.save(icesiAccountDTO);
    }

    @Override
    public String activeAccount(String accountNumber) {
        return "The account " + IcesiAccountService.enableAccount(accountNumber).getAccountNumber() + " is active";
    }

    @Override
    public String inactiveAccount(String accountNumber) {
        return "The account " + IcesiAccountService.disableAccount(accountNumber).getAccountNumber() + " Is disable ";
    }

    @Override
    public String withdrawalAccount(String accountNumber, Long value) {
        return null;
    }

    @Override
    public String depositAccount(String accountNumber, Long value) {
        return null;
    }

    @Override
    public String transferAccount(String accountNumberOrigin, String accountNumberDestination, Long value) {
        return null;
    }
}
