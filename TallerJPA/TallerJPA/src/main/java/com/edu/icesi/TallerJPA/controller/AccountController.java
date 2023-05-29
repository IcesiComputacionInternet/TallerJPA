package com.edu.icesi.TallerJPA.controller;


import com.edu.icesi.TallerJPA.api.AccountAPI;
import com.edu.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.edu.icesi.TallerJPA.dto.IcesiAccountGetDTO;
import com.edu.icesi.TallerJPA.dto.IcesiTransactionDTO;
import com.edu.icesi.TallerJPA.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.edu.icesi.TallerJPA.api.AccountAPI.BASE_ACCOUNT_URL;

@RequestMapping(BASE_ACCOUNT_URL)
@RestController
@AllArgsConstructor
public class AccountController implements AccountAPI {

    private final AccountService accountService;

    @Override
    public IcesiAccountDTO getAccount(String accountNumber) {
        return null;
    }

    @Override
    public List<IcesiAccountDTO> getAllUsers() {
        return null;
    }

    @Override
    public IcesiAccountDTO addAccount(IcesiAccountDTO accountCreateDTO) {
        return accountService.save(accountCreateDTO);
    }

    @Override
    public IcesiTransactionDTO withdraw(IcesiTransactionDTO icesiTransactionDTO) {
        return accountService.withdrawals(icesiTransactionDTO);
    }

    @Override
    public IcesiTransactionDTO deposit(IcesiTransactionDTO icesiTransactionDTO) {
        return accountService.depositMoney(icesiTransactionDTO);
    }

    @Override
    public IcesiTransactionDTO transfer(IcesiTransactionDTO icesiTransactionDTO) {
        return accountService.transferMoney(icesiTransactionDTO);
    }

    @Override
    public IcesiAccountDTO enable(String accountNumber) {
        return accountService.setToEnableState(accountNumber);
    }

    @Override
    public IcesiAccountDTO disable(String accountNumber) {
        return accountService.setToDisableState(accountNumber);
    }

    @Override
    public List<IcesiAccountGetDTO> findByAccounts(UUID idUser) {
        return accountService.findByAccounts(idUser);
    }
}
